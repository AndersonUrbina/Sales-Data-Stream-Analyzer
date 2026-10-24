import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.Comparator;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args){
        System.out.println("\nWelcome to the Java Streams Sales Analyzer!\n");

        String fileName = "sales.csv";
        List<Sale> sales = loadSalesFromFiles(fileName);

        if(sales.isEmpty()){
            System.out.println("No sales found!");
            return;
        }

        displayAllSales(sales);
        displaySalesOver500(sales);
        countSalesOver1000(sales);
        calculateTotalSales(sales);
        calculateAvgSale(sales);
        displayHighestSale(sales);
        displayLowestSale(sales);
        displaySalesRegion(sales, "south");
        displayEmployeeName(sales);
    }

    // List method to get sales from CSV
    public static List<Sale> loadSalesFromFiles(String filename){
        List<Sale> sales = new ArrayList<>();

        try{
            File file = new File(filename);
            Scanner fileReader = new Scanner(file);

            //skip the header row
            if(fileReader.hasNextLine())
                fileReader.nextLine();

            while (fileReader.hasNextLine()){
                String line = fileReader.nextLine();
                String[] parts = line.split(",");

                //Build each sale
                if (parts.length == 4){
                    int saleId = Integer.parseInt(parts[0]);
                    String employeeName = parts[1];
                    String region = parts[2];
                    double amount = Double.parseDouble(parts[3]);

                    Sale sale = new Sale(saleId, employeeName, region, amount);
                    sales.add(sale);
                }

            }
            fileReader.close();

        } catch (FileNotFoundException exception) {
            System.out.println("File error: " + exception.getMessage());
        } catch (NumberFormatException _ ) {
            System.out.println("Number in the CSV file could not be parsed.");
        }

        return sales;
    }

    public static void displayAllSales(List<Sale> sales){
        System.out.println("All sales:");
        System.out.println("-------------");

        sales.stream().forEach(System.out::println);

        System.out.println();
    }

    public static void displaySalesOver500(List<Sale> sales){
        System.out.println("Sales over 500:");

        System.out.println("-------------");
        sales.stream()
                .filter(sale -> sale.getAmount() <= 500)
                .forEach(System.out::println);

        System.out.println();
    }

    public static void countSalesOver1000(List<Sale> sales){
        long count = sales.stream()
                .filter(sale -> sale.getAmount() >= 1000)
                .count();
        System.out.println("Number of sales over $1,000: " + count);
        System.out.println();
    }

    public static void calculateTotalSales(List<Sale> sales){
        double total = sales.stream()
                .mapToDouble(Sale::getAmount)
                .sum();
        System.out.println("Total sales: $" + String.format("%.2f", total));
        System.out.println();
    }

    public static void calculateAvgSale(List<Sale> sales){
        double avg = sales.stream()
                .mapToDouble(Sale::getAmount)
                .average()
                .orElse(0);
        System.out.println("Average sales: $" + String.format("%.2f",avg));
        System.out.println();
    }

    public static void displayHighestSale(List<Sale> sales){
        Optional<Sale> highestSale;
        highestSale = sales.stream()
        .max(Comparator.comparing(Sale::getAmount));

        highestSale.ifPresent(sale-> System.out.println("Highest sale: $" +  sale));
        System.out.println();
    }

    public static void displayLowestSale(List<Sale> sales){
        Optional<Sale> lowestSale = sales.stream()
                .min(Comparator.comparing(Sale::getAmount));

        lowestSale.ifPresent(sale-> System.out.println("Lowest sale: $" +  sale));
        System.out.println();
    }

    public static void displaySalesRegion(List<Sale> sales, String selectedRegion){
        System.out.println("Sales from: " + selectedRegion);
        System.out.println("Regions:");
        System.out.println("------------");
        sales.stream()
                .filter(sale -> sale.getRegion().equalsIgnoreCase(selectedRegion))
                .forEach(System.out::println);

        System.out.println();
    }

    public static void displayEmployeeName(List<Sale> sales){
        System.out.println("Employee Names:");
        System.out.println("------------");
        sales.stream()
                .map(Sale::getEmployeeName)
                .forEach(System.out::println);

        System.out.println();
    }
}
