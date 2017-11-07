package com.goit.jdbc.app;

import com.goit.jdbc.app.dao.DeveloperDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConsoleCRUD {

    static class StubDeveloperDAO implements DeveloperDAO {
        List<Developer> developers = new ArrayList<Developer>();

        public void createDeveloper(Developer developer) {
            developers.add(developer);
        }

        public Developer getDeveloper(long id) {
            return null;
        }

        public void updateDeveloper(Developer developer) {

        }

        public void deleteDeveloper(long id) {

        }

        public List<Developer> listDevelopers() {
            return developers;
        }

        public void addDevelopers(List<Developer> developers) {

        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        DeveloperDAO storage = new StubDeveloperDAO();// new Storage();

        while (sc.hasNextLine()) {
            String cmd = sc.nextLine();
            String[] parts = cmd.split(" ");
            String command = parts[0];

            //create Ivan Vladimirovich Melnichuk
            if (command.equals("create")) {
                if (parts.length < 3) {
                    System.out.println("Wrong format. Usage: create <first_name> <last_name>");
                    continue;
                }

                String firstName = parts[1];
                String lastName = parts[2];

                Developer developer = new Developer();
                developer.setFirstName(firstName);
                developer.setLastName(lastName);
                storage.createDeveloper(developer);
            } else if(command.equals("list")) {
                List<Developer> developers = storage.listDevelopers();
                System.out.println();
                System.out.println();
                System.out.println("ALL DEVELOPERS");
                System.out.println("FIRST_NAME   |   LAST_NAME");
                for(Developer developer : developers) {
                    System.out.println(developer.getFirstName() + "   |    " + developer.getLastName());
                }
                System.out.println();
            }
        }
    }
}
