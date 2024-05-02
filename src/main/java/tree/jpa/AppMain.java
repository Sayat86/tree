package tree.jpa;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.Scanner;

public class AppMain {
    public static void main(String[] args) {

        while (true) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Введите вид действия");
            System.out.println("1 - Создание категории");
            System.out.println("2 - Удаление категории");
            System.out.println("3 - Перемещение категории");
            System.out.println("4 - Выйти из программы");
            int cat = Integer.parseInt(scanner.nextLine());

            if (cat == 4) {
                System.out.println("Вы вышли из программы");
                break;
            }

            switch (cat) {
                case 1:
                    CreateCategoryQuery.create();
                    break;

                case 2:
                    DeleteCategoryQuery.delete();
                    break;

                case 3:
                    MoveCategory.move();
                    break;

                default:
                    System.out.println("Вы ввели не правильное значение");
                    break;
            }
        }
    }
}
