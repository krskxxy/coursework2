import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedWriter;
import java.io.FileWriter;

int cardCompare(String card1, String card2) {
        char suit1 = card1.charAt(card1.length() - 1);
        char suit2 = card2.charAt(card2.length() - 1);
        int number1 = Integer.parseInt(card1.substring(0, card1.length() - 1));
        int number2 = Integer.parseInt(card2.substring(0, card2.length() - 1));
        String suitPriority = "HCDS";
        int suitComparison = suitPriority.indexOf(suit1) - suitPriority.indexOf(suit2);
        if (suitComparison != 0) {
            return suitComparison < 0 ? -1 : 1;
        }
        if (number1 < number2) {
            return -1;
        } else if (number1 > number2) {
            return 1;
        } else {
            // Both suit and number are equal
            return 0;
        }
    }

   ArrayList<String> bubbleSort(ArrayList<String> cards) {
        boolean swapped;
        do {
            swapped = false;
            for (int i = 0; i < cards.size() - 1; i++) {
                if (cardCompare(cards.get(i), cards.get(i + 1)) > 0) {
                    // Swap the cards
                    String temp = cards.get(i);
                    cards.set(i, cards.get(i + 1));
                    cards.set(i + 1, temp);
                    swapped = true;
                }
            }
        } while (swapped);
        return cards;
    }

   long measureBubbleSort(String filename) {
        try {
            List<String> cardLines = Files.readAllLines(Paths.get(filename));
            ArrayList<String> cards = new ArrayList<>(cardLines);
            long startTime = System.currentTimeMillis();
            bubbleSort(cards);
            long endTime = System.currentTimeMillis();
            return endTime - startTime;
        } catch (IOException e) {
            e.printStackTrace();
            return -1; 
        }
    }

List<String> mergeSort(List<String> list) {
        if (list.size() <= 1) {
            return list;
        }
        int middle = list.size() / 2;
        List<String> left = new ArrayList<>(list.subList(0, middle));
        List<String> right = new ArrayList<>(list.subList(middle, list.size()));
        left = mergeSort(left);
        right = mergeSort(right);
        return merge(left, right);
    }
   List<String> merge(List<String> left, List<String> right) {
        List<String> merged = new ArrayList<>();
        int i = 0, j = 0;
        while (i < left.size() && j < right.size()) {
            if (cardCompare(left.get(i), right.get(j)) <= 0) {
                merged.add(left.get(i));
                i++;
            } else {
                merged.add(right.get(j));
                j++;
            }
        }
        while (i < left.size()) {
            merged.add(left.get(i));
            i++;
        }

        while (j < right.size()) {
            merged.add(right.get(j));
            j++;
        }

        return merged;
    }

  long measureMergeSort(String filename) {
        List<String> cards = new ArrayList<>();
        try {
            // Read all lines from the file into a list of strings
            cards = Files.readAllLines(Paths.get(filename));
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
        long startTime = System.currentTimeMillis();
        List<String> sortedCards = mergeSort(cards);
        long endTime = System.currentTimeMillis();
        return endTime - startTime;
    }


void sortComparison(String[] fileNames) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter("sortComparison.csv"))) {
        // 写入标题行
        writer.write("Algorithm,Size 10,Size 100,Size 1000\n");

        // 写入 Bubble Sort 的数据
        writer.write("Bubble Sort");
        for (String fileName : fileNames) {
            long bubbleSortTime = measureBubbleSort(fileName);
            writer.write("," + bubbleSortTime);
        }
        writer.write("\n");

        // 写入 Merge Sort 的数据
        writer.write("Merge Sort");
        for (String fileName : fileNames) {
            long mergeSortTime = measureMergeSort(fileName);
            writer.write("," + mergeSortTime);
        }
        writer.write("\n");
    } catch (IOException e) {
        e.printStackTrace();
    }
}
