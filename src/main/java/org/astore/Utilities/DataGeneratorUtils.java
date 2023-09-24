package org.astore.Utilities;

import com.github.javafaker.Faker;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class DataGeneratorUtils {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyMMdd");
    private static Faker faker = new Faker();
    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int MIN_LENGTH = 3;


    public static String generatePassword(int minChars, int maxChars) {
        Random random = new Random();
        int length = random.nextInt(maxChars - minChars + 1) + minChars;
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(CHARACTERS.length());
            stringBuilder.append(CHARACTERS.charAt(index));
        }

        return stringBuilder.toString();
    }

    public static String generateFirstName() {
        String firstName;

        do {
            firstName = faker.name().firstName();
        } while (firstName.length() < 3 || firstName.length() > 14);

        return firstName;
    }

    public static String generateLastName() {
        String lastName;

        do {
            lastName = faker.name().lastName();
        } while (lastName.length() < 3 || lastName.length() > 10);

        return lastName;
    }

    public static String generateInstalmentAmount() {
        double maxInstallmentAmount = 1000000.0; // 1 million
        double minInstallmentAmount = 1;         // 1 (greater than zero)

        Random random = new Random();
        double instalmentAmount = minInstallmentAmount + (random.nextDouble() * (maxInstallmentAmount - minInstallmentAmount));

        // Round to two decimal places
        instalmentAmount = Math.round(instalmentAmount * 100.0) / 100.0;

        // Convert to String representation and return
        return String.valueOf(instalmentAmount);
    }

    public static String generateInitialAmount() {
        double maxInitialAmount = 1000000.0; // 1 million
        double minInitialAmount = 1;         // 1 (greater than zero)

        Random random = new Random();
        double initialAmount = minInitialAmount + (random.nextDouble() * (maxInitialAmount - minInitialAmount));

        // Round to two decimal places
        initialAmount = Math.round(initialAmount * 100.0) / 100.0;

        // Convert to String representation and return
        return String.valueOf(initialAmount);
    }

    public static String generateSAIDNumber() {
        // Generate a random birth date before today's date
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -18); // Subtract 18 years
        Date currentDate = calendar.getTime();
        Date birthDate = generateRandomDate(currentDate);

        // Format the birth date as YYMMDD
        String formattedDate = DATE_FORMAT.format(birthDate);

        // Generate random digits for the rest of the ID number
        Random random = new Random();
        int genderNumber = random.nextInt(10000); // 4-digit random sequence number Random gender number (0-9)
        int citizenshipNumber = random.nextInt((1 - 0) + 1); // Random citizenship number (0-9)
        int checkSum = random.nextInt(10); // checksum digit (0-9)
        // Concatenate all the parts to form the ID number
        String idNumber = formattedDate + genderNumber + citizenshipNumber + "8" + checkSum;

        return idNumber;
    }

    public static String generateSouthAfricanCellphoneNumber() {
        String prefix = "07";
        String number = faker.numerify("########"); // Generates a random 8-digit number
        return prefix + number;
    }


    public static String generateEmailAddress() {
        return faker.internet().emailAddress();
    }

    private static Date generateRandomDate(Date maxDate) {
        Random random = new Random();
        long maxMillis = maxDate.getTime();
        long randomMillis = (long) (random.nextDouble() * maxMillis);
        return new Date(randomMillis);
    }


    // Generate a random SA passport number
    public static String generateSAPassportNumber() {
        StringBuilder passportNumber = new StringBuilder();
        Random random = new Random();

        // The first character is always 'A'
        passportNumber.append('A');

        // The rest of the passport number is numeric (0-9)
        for (int i = 0; i < 8; i++) {
            passportNumber.append(random.nextInt(10));
        }

        return passportNumber.toString();
    }

    // Generate a random SA Temporary Residence ID
    public static String generateSATemporaryResidenceID() {
        StringBuilder temporaryResidenceID = new StringBuilder();
        Random random = new Random();

        // The first character is 'T' or 't'
        char firstChar = (char) (random.nextInt(2) + 'T');
        temporaryResidenceID.append(firstChar);

        // The rest of the ID is numeric (0-9)
        for (int i = 0; i < 11; i++) {
            temporaryResidenceID.append(random.nextInt(10));
        }

        return temporaryResidenceID.toString();
    }

    public static String generateLongID() {
        int targetLength = 34; // You can adjust the desired length
        StringBuilder idBuilder = new StringBuilder();

        Random random = new Random();
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

        while (idBuilder.length() < targetLength) {
            int index = random.nextInt(characters.length());
            char randomChar = characters.charAt(index);
            idBuilder.append(randomChar);
        }

        return idBuilder.toString();
    }

    public static String generateShortMobileNum() {
        Random random = new Random();
        int maxDigits = 9; // Maximum number of digits
        int numDigits = random.nextInt(maxDigits) + 1; // Generate a random number of digits between 1 and 9

        StringBuilder numberBuilder = new StringBuilder();

        for (int i = 0; i < numDigits; i++) {
            int digit = random.nextInt(10); // Generate a random digit between 0 and 9
            numberBuilder.append(digit);
        }

        return numberBuilder.toString();
    }


}
