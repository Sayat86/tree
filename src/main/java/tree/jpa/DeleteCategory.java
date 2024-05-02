package tree.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import tree.jpa.entity.Category;

import java.util.List;
import java.util.Scanner;

public class DeleteCategory {
    public static void main(String[] args) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("main");
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

            TypedQuery<Category> categoryTypedQuery = manager.createQuery("select c from Category c " +
                    "where c.leftKey >= ?1 and c.rightKey <= ?2", Category.class);
            categoryTypedQuery.setParameter(1, left);
            categoryTypedQuery.setParameter(2, right);
            List<Category> categoryList = categoryTypedQuery.getResultList();
            for (int i = 0; i < categoryList.size(); i++) {
                System.out.println(categoryList.get(i).getName());
                manager.remove(categoryList.get(i));
            }
            System.out.println();

            TypedQuery<Category> categoryTypedQuery2 = manager.createQuery("select c from Category c", Category.class);
            List<Category> categoryList2 = categoryTypedQuery2.getResultList();
            for (int i = 0; i < categoryList2.size(); i++) {
                System.out.println(categoryList2.get(i).getName());

                if (categoryList2.get(i).getLeftKey() < left) {
                    categoryList2.get(i).setRightKey(categoryList2.get(i).getRightKey() - ((right - left) + 1));
                }
                else {
                    categoryList2.get(i).setLeftKey(categoryList2.get(i).getLeftKey() - ((right - left) + 1));
                    categoryList2.get(i).setRightKey(categoryList2.get(i).getRightKey() - ((right - left) + 1));
                }
            }

            manager.getTransaction().commit();
        } catch (Exception e) {
            manager.getTransaction().rollback();
            throw new RuntimeException(e);
        }
        manager.close();
        factory.close();
    }
}
