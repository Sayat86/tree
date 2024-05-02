package tree.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import tree.jpa.entity.Category;

import java.util.List;
import java.util.Scanner;

public class CreateCategory {
    public static void main(String[] args) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("main");
        EntityManager manager = factory.createEntityManager();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите ID категории");
        String categoryId = scanner.nextLine();
        Category cat = manager.find(Category.class, categoryId);
        Integer rightKey = cat.getRightKey();

        System.out.println(rightKey);


        try {
            manager.getTransaction().begin();

            TypedQuery<Category> findRightMoor = manager.createQuery("select c from Category c " +
                    "where c.rightKey >= ?1", Category.class);
            findRightMoor.setParameter(1, rightKey);
            List<Category> categoryList = findRightMoor.getResultList();
            for (int i = 0; i < categoryList.size(); i++) {
                System.out.print(categoryList.get(i).getName() + "/");

                if (categoryList.get(i).getLeftKey() > rightKey) {
                    System.out.print(categoryList.get(i).getLeftKey() + 2);
                    System.out.println(" right " + (categoryList.get(i).getRightKey() + 2));
                    categoryList.get(i).setLeftKey(categoryList.get(i).getLeftKey() + 2);
                    categoryList.get(i).setRightKey(categoryList.get(i).getRightKey() + 2);
                }
                else {
                    System.out.print("left " + categoryList.get(i).getLeftKey());
                    System.out.println(" right " + (categoryList.get(i).getRightKey() + 2));
                    categoryList.get(i).setLeftKey(categoryList.get(i).getLeftKey());
                    categoryList.get(i).setRightKey(categoryList.get(i).getRightKey() + 2);
                }
            }


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

            manager.getTransaction().commit();
        } catch (Exception e) {
            manager.getTransaction().rollback();
            throw new RuntimeException(e);
        }
        manager.close();
        factory.close();
    }
}
