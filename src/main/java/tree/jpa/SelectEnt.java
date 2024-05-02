package tree.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import tree.jpa.entity.Category;

import java.util.List;

public class SelectEnt {
    public static void main(String[] args) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("main");
        EntityManager manager = factory.createEntityManager();

        TypedQuery<Category> categoryTypedQuery = manager.createQuery("select c from Category c", Category.class);
        List<Category> categoryList = categoryTypedQuery.getResultList();
        for (int i = 0; i < categoryList.size(); i++) {
            for (int j = 0; j < categoryList.get(i).getLevel(); j++) {
                System.out.print("-");
            }
            System.out.println(categoryList.get(i).getName() + ":" + categoryList.get(i).getLeftKey() + "/"
                    + categoryList.get(i).getRightKey());
        }

    }
}
