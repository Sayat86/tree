package tree.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import tree.jpa.entity.Category;

import java.util.List;
import java.util.Scanner;

public class Sel {
    public static void main(String[] args) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("main");
        EntityManager manager = factory.createEntityManager();

        Scanner scanner = new Scanner(System.in);

        TypedQuery<Category> categoryTypQ = manager.createQuery("select c from Category c", Category.class);
        List<Category> cat = categoryTypQ.getResultList();
        for (int i = 0; i < cat.size(); i++) {
            System.out.println(cat.get(i).getName());
        }

        System.out.println("Введите категорию");
        String categoryName = scanner.nextLine();
        TypedQuery<Category> categoryTypedQuery = manager.createQuery("select c from Category c " +
                "where c.name = ?1", Category.class);
        categoryTypedQuery.setParameter(1, categoryName);
        Category categorySin = categoryTypedQuery.getSingleResult();

        System.out.println(categorySin.getName());

        Integer left = categorySin.getLeftKey();
        Integer right = categorySin.getRightKey();


        TypedQuery<Category> categoryTypedQuery2 = manager.createQuery("select c from Category c " +
                "where c.leftKey > ?1 and c.rightKey < ?2", Category.class);
        categoryTypedQuery2.setParameter(1, left);
        categoryTypedQuery2.setParameter(2, right);
        List<Category> categoryList = categoryTypedQuery2.getResultList();
        for (int i = 0; i < categoryList.size(); i++) {
            for (int j = 0; j < categoryList.get(i).getLevel(); j++) {
                System.out.print("-");
            }
            System.out.println(categoryList.get(i).getName());
        }
    }
}
