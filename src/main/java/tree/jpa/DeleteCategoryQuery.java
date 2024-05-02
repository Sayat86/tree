package tree.jpa;

import jakarta.persistence.*;
import tree.jpa.entity.Category;

import java.util.List;
import java.util.Scanner;

public class DeleteCategoryQuery {

        public static void delete() {
            EntityManagerFactory factory = CentralFactory.createManager();
        EntityManager manager = factory.createEntityManager();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите ID категории, которую хотите удалить");
        String categoryId = scanner.nextLine();
        Category cat = manager.find(Category.class, categoryId);

        Integer left = cat.getLeftKey();
        Integer right = cat.getRightKey();
        System.out.println(left + "/" + right);

        try {
            manager.getTransaction().begin();

            Query query = manager.createQuery("delete from Category c where c.leftKey >= ?1 and c.rightKey <= ?2");
            query.setParameter(1, left);
            query.setParameter(2, right);
            query.executeUpdate();

            Query query1 = manager.createQuery("update Category c set c.leftKey = c.leftKey - ((?1 - ?2) + 1)" +
                    "where c.leftKey > ?2");
            query1.setParameter(1, right);
            query1.setParameter(2, left);
            query1.executeUpdate();

            Query query2 = manager.createQuery("update Category c set c.rightKey = c.rightKey - ((?1 - ?2) + 1)");
            query2.setParameter(1, right);
            query2.setParameter(2, left);
            query2.executeUpdate();


            manager.getTransaction().commit();
        } catch (Exception e) {
            manager.getTransaction().rollback();
            throw new RuntimeException(e);
        }
        manager.close();
    }
}
