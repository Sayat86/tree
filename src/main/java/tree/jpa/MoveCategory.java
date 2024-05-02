package tree.jpa;

import jakarta.persistence.*;
import tree.jpa.entity.Category;

import java.util.List;
import java.util.Scanner;

public class MoveCategory {

        public static void move() {
            EntityManagerFactory factory = CentralFactory.createManager();
        EntityManager manager = factory.createEntityManager();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите ID категории, которую хотите переместить");
        String categoryId = scanner.nextLine();
        Category cat = manager.find(Category.class, categoryId);


        try {
            manager.getTransaction().begin();

                Integer left = cat.getLeftKey();
                Integer right = cat.getRightKey();
                Integer level = cat.getLevel();

                System.out.println(left + "/" + right + "/" + level);

                Query query = manager.createQuery("update Category c set c.leftKey = c.leftKey * -1 " +
                        "where c.leftKey >= ?2 and c.leftKey < ?1");
                query.setParameter(1, right);
                query.setParameter(2, left);
                query.executeUpdate();

                Query query1 = manager.createQuery("update Category c set c.rightKey = c.rightKey * -1 " +
                        "where c.rightKey <= ?1 and c.rightKey > ?2");
                query1.setParameter(1, right);
                query1.setParameter(2, left);
                query1.executeUpdate();

                Query query2 = manager.createQuery("update Category c set c.leftKey = c.leftKey - ((?1 - ?2) + 1)" +
                        "where c.leftKey > ?2");
                query2.setParameter(1, right);
                query2.setParameter(2, left);
                query2.executeUpdate();

                Query query3 = manager.createQuery("update Category c set c.rightKey = c.rightKey - ((?1 - ?2) + 1)" +
                        "where c.rightKey > ?1");
                query3.setParameter(1, right);
                query3.setParameter(2, left);
                query3.executeUpdate();

                System.out.println("Введите ID категории, куда хотите переместить");
                String categoryId2 = scanner.nextLine();
                Category cat2 = manager.find(Category.class, categoryId2);


                if (cat2 == null) {
                    TypedQuery<Integer> categoryTypedQuery = manager.createQuery("select max(c.rightKey) from Category c", Integer.class);
                    int catOne = categoryTypedQuery.getSingleResult();
                    System.out.println(catOne);

                    Query query11 = manager.createQuery("update Category c set c.level = c.level - ?1" +
                            "where c.leftKey < 0");
                    query11.setParameter(1, level);
                    query11.executeUpdate();

                    Query query9 = manager.createQuery("update Category c set c.leftKey = (c.leftKey * -1) + (?1 - ?2) + 1" +
                            "where c.leftKey < 0");
                    query9.setParameter(1, catOne);
                    query9.setParameter(2, left);
                    query9.executeUpdate();

                    Query query10 = manager.createQuery("update Category c set c.rightKey = (c.rightKey * -1) + (?1 - ?2) + 1" +
                            "where c.rightKey < 0");
                    query10.setParameter(1, catOne);
                    query10.setParameter(2, left);
                    query10.executeUpdate();
                }
                else {

                Integer left2 = cat2.getLeftKey();
                Integer right2 = cat2.getRightKey();
                Integer level2 = cat2.getLevel();

                System.out.println(left2 + "/" + right2 + "/" + level2);

                Query query4 = manager.createQuery("update Category c set c.leftKey = c.leftKey + ((?1 - ?2) + 1)" +
                        "where c.leftKey > ?3");
                query4.setParameter(1, right);
                query4.setParameter(2, left);
                query4.setParameter(3, left2);
                query4.executeUpdate();

                Query query5 = manager.createQuery("update Category c set c.rightKey = c.rightKey + ((?1 - ?2) + 1)" +
                        "where c.rightKey > ?3");
                query5.setParameter(1, right);
                query5.setParameter(2, left);
                query5.setParameter(3, left2);
                query5.executeUpdate();

                Query query8 = manager.createQuery("update Category c set c.level = c.level + (?1 - ?2) + 1" +
                        "where c.leftKey < 0");
                query8.setParameter(1, level2);
                query8.setParameter(2, level);
                query8.executeUpdate();

                Query query6 = manager.createQuery("update Category c set c.leftKey = (c.leftKey * -1) + (?1 - ?2) + 1" +
                        "where c.leftKey < 0");
                query6.setParameter(1, left2);
                query6.setParameter(2, left);
                query6.executeUpdate();

                Query query7 = manager.createQuery("update Category c set c.rightKey = (c.rightKey * -1) + (?1 - ?2) + 1" +
                        "where c.rightKey < 0");
                query7.setParameter(1, left2);
                query7.setParameter(2, left);
                query7.executeUpdate();
                }

            manager.getTransaction().commit();
        } catch (Exception e) {
            manager.getTransaction().rollback();
            throw new RuntimeException(e);
        }
        manager.close();
    }
}
