package tree.jpa;

import jakarta.persistence.*;
import org.hibernate.sql.Update;
import tree.jpa.entity.Category;

import java.util.List;
import java.util.Scanner;

public class CreateCategoryQuery {

        public static void create() {
            EntityManagerFactory factory = CentralFactory.createManager();
            EntityManager manager = factory.createEntityManager();

            Scanner scanner = new Scanner(System.in);
            System.out.println("Введите ID категории в которой хотите создать подкатегорию");
            String categoryId = scanner.nextLine();
            Category cat = manager.find(Category.class, categoryId);


            try {
                manager.getTransaction().begin();

                if (cat == null) {
                    System.out.println("Введите название категории");
                    String categoryName = scanner.nextLine();
                    TypedQuery<Integer> categoryTypedQuery = manager.createQuery("select max(c.rightKey) from Category c", Integer.class);
                    int catOne = categoryTypedQuery.getSingleResult();
                    System.out.println(catOne);
                    Category category = new Category();
                    category.setName(categoryName);
                    category.setLeftKey(catOne + 1);
                    category.setRightKey(catOne + 2);
                    category.setLevel(cat.getLevel());
                    manager.persist(category);
                }
                else {
                    Integer rightKey = cat.getRightKey();

                    System.out.println(rightKey);

                    Query query = manager.createQuery("update Category c set c.leftKey = c.leftKey + 2 where c.leftKey > ?1");
                    query.setParameter(1, rightKey);
                    query.executeUpdate();

                    Query query2 = manager.createQuery("update Category c set c.rightKey = c.rightKey + 2 " +
                            "where c.rightKey >= ?1");
                    query2.setParameter(1, rightKey);
                    query2.executeUpdate();


                    System.out.println("Введите название категории");
                    String categoryName = scanner.nextLine();

                    System.out.print("объект левый-" + rightKey);
                    System.out.println("объект правый-" + (rightKey + 1));
                    System.out.println("level - " + (cat.getLevel() + 1));

                    Category category = new Category();
                    category.setName(categoryName);
                    category.setLeftKey(rightKey);
                    category.setRightKey(rightKey + 1);
                    category.setLevel(cat.getLevel() + 1);
                    manager.persist(category);
                }

                manager.getTransaction().commit();
            } catch (Exception e) {
                manager.getTransaction().rollback();
                throw new RuntimeException(e);
            }
            manager.close();
        }

    }
