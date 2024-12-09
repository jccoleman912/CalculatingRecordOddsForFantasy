import java.util.InputMismatchException;
import java.util.Scanner;

public class FantasyTeamProbabilityOfRecordsCalculator {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int[] arr = getOpeningInformation(scanner);

        int numOfTeams = arr[0], numOfWeeks = arr[1];

        int[] weeklyFinishesArray = new int[numOfWeeks];

        for (int i = 1; i <= numOfWeeks; i++) {
            weeklyFinishesArray[i - 1] = askForWeeklyFinishes(scanner, i, numOfTeams);
        }

        double[] finalWinOdds = new double[numOfWeeks + 1];

        finalWinOdds = getOddsOfEveryRecord(weeklyFinishesArray, numOfWeeks, numOfTeams);
        printFinalWinOdds(numOfWeeks, finalWinOdds);

        scanner.close();
    }


    private static int[] getOpeningInformation(Scanner scanner) {
        int numOfPlayers, numOfWeeks;
        while(true) {
            try {
                System.out.println("How many players are in your league?");
                numOfPlayers = scanner.nextInt();
                break;
            } catch (Exception InputMismatchException) {
                System.out.println("Please input an accurate integer.");
                scanner.nextLine();
            }
        }

        while(true) {
            try {
                System.out.println("How many weeks are in your regular season?");
                numOfWeeks = scanner.nextInt();
                if(numOfWeeks < 17 && numOfWeeks > 0) {
                    break;
                } else {
                    System.out.println("Please input an acceptable amount of weeks in the regular season.");
                }
            } catch (Exception InputMismatchException) {
                System.out.println("Please input an accurate integer.");
                scanner.nextLine();
            }
        }

        int[] arr = {numOfPlayers, numOfWeeks};

        return arr;
    }

    private static int askForWeeklyFinishes(Scanner scanner, int currentWeek, int numOfTeams) {

        int weeklyFinish;
        while(true) {
            try {
                System.out.print("Week " + currentWeek + ": ");
                weeklyFinish = scanner.nextInt();
                if(weeklyFinish <= numOfTeams && weeklyFinish > 0) {
                    break;
                } else {
                    System.out.println("Please input an acceptable standing for the given week.");
                }
            } catch (Exception InputMismatchException) {
                System.out.println("Please input an accurate integer.");
                scanner.nextLine();
            }
        }

        return weeklyFinish;
    }

    private static double[] getOddsOfEveryRecord(int[] weeklyFinishes, int numOfWeeks, int numOfTeams) {

        StringBuilder stringBuilder = new StringBuilder(numOfWeeks);

        double[] winPercentages = new double[numOfWeeks + 1];

        for(int i = 0; i < Math.pow(2, numOfWeeks); i++) {
            stringBuilder.delete(0, stringBuilder.length());
            String binaryOutput = Integer.toBinaryString(i);

            if(binaryOutput.length() < numOfWeeks) {
                int difference = numOfWeeks - binaryOutput.length();

                for(int j = 0; j < difference; j++) {
                    stringBuilder.append(0);
                }
            }
            stringBuilder.append(binaryOutput);

            double winningOdds = processSpecificOutcomeOdds(stringBuilder, weeklyFinishes, numOfTeams);
            int numOfWinsInConfiguration = getNumOfWinsInConfiguration(stringBuilder);

            winPercentages[numOfWinsInConfiguration] += winningOdds;

        }

        return winPercentages;
    }

    private static double processSpecificOutcomeOdds(StringBuilder stringBuilder, int[] weeklyFinishes, int numOfTeams) {

        double oddsOfThisRecord = 1;

        for(int i = 0; i < stringBuilder.length(); i++) {
            double losingWeekOdds = ((double) weeklyFinishes[i] - 1) / (numOfTeams - 1);
            if(stringBuilder.charAt(i) == '1') {
                oddsOfThisRecord *= (1 - losingWeekOdds);
            } else {
                oddsOfThisRecord *= (losingWeekOdds);
            }
        }

        return oddsOfThisRecord;
    }

    private static int getNumOfWinsInConfiguration(StringBuilder stringBuilder) {
        int winTotal = 0;
        for(char c : stringBuilder.toString().toCharArray()) {
            winTotal += Character.getNumericValue(c);
        }
        return winTotal;
    }

    private static void printFinalWinOdds(int numOfWeeks, double[] winOdds) {

        System.out.println();
        System.out.println("Here are the odds of you getting every possible record.");
        System.out.println();

        for (int i = 0; i <= numOfWeeks; i++) {
            System.out.println("Odds of going " + i + "-" + (numOfWeeks - i) + ":");
            double probability = winOdds[i];
            probability *= 100000.0;
            probability = Math.round(probability);
            probability /= 1000;
            System.out.println(probability + "%");
        }
    }
}