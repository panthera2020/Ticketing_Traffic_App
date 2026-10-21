package com.lastma;

import com.lastma.controllers.ChallengeController;
import com.lastma.controllers.DriverController;
import com.lastma.controllers.OffenceController;
import com.lastma.controllers.OfficerController;
import com.lastma.controllers.PaymentController;
import com.lastma.controllers.TicketController;
import com.lastma.controllers.VehicleController;
import com.lastma.data.models.OffenderStatus;
import com.lastma.data.models.VehicleType;
import com.lastma.data.models.ViolationType;
import com.lastma.dtos.requests.CreateOffenceRequest;
import com.lastma.dtos.requests.FindAllTicketsForVehicleRequest;
import com.lastma.dtos.requests.FindOffenceByCodeRequest;
import com.lastma.dtos.requests.FindOffenceByIdRequest;
import com.lastma.dtos.requests.FindTicketByIdRequest;
import com.lastma.dtos.requests.FindTicketByNumberRequest;
import com.lastma.dtos.requests.FindVehicleByPlateNumberRequest;
import com.lastma.dtos.requests.IssueTicketRequest;
import com.lastma.dtos.requests.MakePaymentRequest;
import com.lastma.dtos.requests.RegisterDriverRequest;
import com.lastma.dtos.requests.RegisterOfficerRequest;
import com.lastma.dtos.requests.RegisterVehicleRequest;
import com.lastma.dtos.requests.ReviewChallengeRequest;
import com.lastma.dtos.requests.SubmitChallengeRequest;
import com.lastma.dtos.responses.ApiResponse;
import com.lastma.dtos.responses.FindAllOfficersResponse;
import com.lastma.dtos.responses.FindAllPaymentsResponse;
import com.lastma.dtos.responses.FindTicketByIdResponse;
import com.lastma.dtos.responses.FindVehicleByPlateNumberResponse;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final DriverController driverController = new DriverController();
    private static final VehicleController vehicleController = new VehicleController();
    private static final OffenceController offenceController = new OffenceController();
    private static final OfficerController officerController = new OfficerController();
    private static final TicketController ticketController = new TicketController();
    private static final PaymentController paymentController = new PaymentController();
    private static final ChallengeController challengeController = new ChallengeController();

    public static void main(String[] args) {
        String mainMenu = """
                =====================================
                LASTMA TRAFFIC INFRACTION MANAGEMENT
                =====================================
                1. Register motorist
                2. Register vehicle
                3. Manage offence catalogue
                4. Register traffic officer
                5. Issue traffic ticket
                6. Find ticket
                7. View vehicle ticket history
                8. Record simulated payment
                9. Submit a ticket challenge
                10. Review a challenge
                11. View simple summaries
                0. Exit

                =====================================
                Choose an option:
                =====================================
                """;

        boolean running = true;
        while (running) {
            System.out.println();
            System.out.print(mainMenu);
            int choice = readInt("> ");
            System.out.println();
            switch (choice) {
                case 1 -> registerMotorist();
                case 2 -> registerVehicle();
                case 3 -> manageOffenceCatalogue();
                case 4 -> registerOfficer();
                case 5 -> issueTicket();
                case 6 -> findTicket();
                case 7 -> viewVehicleTicketHistory();
                case 8 -> recordSimulatedPayment();
                case 9 -> submitChallenge();
                case 10 -> reviewChallenge();
                case 11 -> viewSummaries();
                case 0 -> running = false;
                default -> System.out.println("Invalid option. Please choose a number from the menu.");
            }
        }
        System.out.println("Goodbye.");
    }

    private static void registerMotorist() {
        System.out.println("--- Register motorist ---");
        RegisterDriverRequest request = new RegisterDriverRequest();
        request.setFullName(readText("Full name: "));
        request.setMail(readText("Email: "));
        request.setPhoneNumber(readText("Phone number: "));
        request.setPassword(readText("Password: "));
        showResult(driverController.registerDriver(request));
    }

    private static void registerVehicle() {
        System.out.println("--- Register vehicle ---");
        RegisterVehicleRequest request = new RegisterVehicleRequest();
        request.setDriverId(readInt("Motorist (driver) id: "));
        request.setMake(readText("Make: "));
        request.setModel(readText("Model: "));
        request.setYear(readText("Year (e.g. 2022): "));
        request.setPlateNumber(readText("Plate number: "));
        VehicleType[] types = VehicleType.values();
        System.out.println("Vehicle types:");
        for (int index = 0; index < types.length; index++) {
            System.out.println("  " + (index + 1) + ". " + types[index]);
        }
        request.setVehicleType(types[readChoice("Choose vehicle type: ", types.length) - 1]);
        showResult(vehicleController.registerVehicle(request));
    }

    private static void manageOffenceCatalogue() {
        String menu = """
                --- Offence catalogue ---
                1. Create offence
                2. Find offence by id
                3. Find offence by code
                0. Back
                """;
        System.out.print(menu);
        int choice = readInt("> ");
        switch (choice) {
            case 1 -> createOffence();
            case 2 -> {
                FindOffenceByIdRequest request = new FindOffenceByIdRequest();
                request.setOffenceId(readInt("Offence id: "));
                showResult(offenceController.findById(request));
            }
            case 3 -> {
                FindOffenceByCodeRequest request = new FindOffenceByCodeRequest();
                request.setCode(readText("Offence code: "));
                showResult(offenceController.findByCode(request));
            }
            case 0 -> { }
            default -> System.out.println("Invalid option.");
        }
    }

    private static void createOffence() {
        ViolationType[] violations = ViolationType.values();
        System.out.println("Violation types:");
        for (int index = 0; index < violations.length; index++) {
            ViolationType violation = violations[index];
            System.out.println("  " + (index + 1) + ". " + violation.getDisplayName()
                    + " (first offence: N" + violation.getFirstOffenceFine() + ")");
        }
        CreateOffenceRequest request = new CreateOffenceRequest();
        request.setViolationType(violations[readChoice("Choose violation type: ", violations.length) - 1]);
        request.setCode(readText("Offence code: "));
        showResult(offenceController.createOffenceWith(request));
    }

    private static void registerOfficer() {
        System.out.println("--- Register traffic officer ---");
        RegisterOfficerRequest request = new RegisterOfficerRequest();
        request.setFullName(readText("Full name: "));
        request.setOfficerNumber(readText("Officer number: "));
        request.setPassword(readText("Password: "));
        showResult(officerController.registerOfficer(request));
    }

    private static void issueTicket() {
        System.out.println("--- Issue traffic ticket ---");
        IssueTicketRequest request = new IssueTicketRequest();
        request.setTicketNumber(readText("Ticket number: "));
        request.setVehicleId(readInt("Vehicle id: "));
        request.setOffenceId(readInt("Offence id: "));
        request.setOfficerId(readInt("Officer id: "));
        request.setLocation(readText("Location: "));
        System.out.println("Offender status:");
        System.out.println("  1. First time offender");
        System.out.println("  2. Subsequent offender");
        int status = readChoice("Choose offender status: ", 2);
        request.setOffenderStatus(status == 1 ? OffenderStatus.FIRST_TIME_OFFENDER : OffenderStatus.SUBSEQUENT_OFFENDER);
        showResult(ticketController.issueTicket(request));
    }

    private static void findTicket() {
        System.out.println("--- Find ticket ---");
        System.out.println("  1. By ticket id");
        System.out.println("  2. By ticket number");
        int choice = readChoice("Choose: ", 2);
        if (choice == 1) {
            FindTicketByIdRequest request = new FindTicketByIdRequest();
            request.setTicketId(readInt("Ticket id: "));
            showResult(ticketController.findTicketById(request));
        } else {
            FindTicketByNumberRequest request = new FindTicketByNumberRequest();
            request.setTicketNumber(readText("Ticket number: "));
            showResult(ticketController.findTicketByCode(request));
        }
    }

    private static void viewVehicleTicketHistory() {
        System.out.println("--- Vehicle ticket history ---");
        System.out.println("  1. By vehicle id");
        System.out.println("  2. By plate number");
        int choice = readChoice("Choose: ", 2);
        int vehicleId;
        if (choice == 1) {
            vehicleId = readInt("Vehicle id: ");
        } else {
            FindVehicleByPlateNumberRequest plateRequest = new FindVehicleByPlateNumberRequest();
            plateRequest.setPlateNumber(readText("Plate number: "));
            ApiResponse vehicleResponse = vehicleController.findVehicleByPlateNumber(plateRequest);
            if (!vehicleResponse.isStatus()) {
                showResult(vehicleResponse);
                return;
            }
            vehicleId = ((FindVehicleByPlateNumberResponse) vehicleResponse.getData()).getId();
        }
        FindAllTicketsForVehicleRequest request = new FindAllTicketsForVehicleRequest();
        request.setVehicleId(vehicleId);
        showResult(ticketController.findAllTicketsFor(request));
    }

    private static void recordSimulatedPayment() {
        System.out.println("--- Record simulated payment ---");
        FindTicketByIdRequest findRequest = new FindTicketByIdRequest();
        findRequest.setTicketId(readInt("Ticket id: "));
        ApiResponse ticketResponse = ticketController.findTicketById(findRequest);
        if (!ticketResponse.isStatus()) {
            showResult(ticketResponse);
            return;
        }
        FindTicketByIdResponse ticket = (FindTicketByIdResponse) ticketResponse.getData();
        System.out.println("Ticket " + ticket.getTicketNumber() + " | status: " + ticket.getStatus()
                + " | amount due: N" + ticket.getViolationFee());
        String confirm = readText("Record a simulated payment of N" + ticket.getViolationFee() + "? (y/n): ");
        if (!confirm.equalsIgnoreCase("y")) {
            System.out.println("Payment cancelled.");
            return;
        }
        MakePaymentRequest request = new MakePaymentRequest();
        request.setTicketId(ticket.getId());
        request.setAmount(ticket.getViolationFee());
        request.setPaymentReference("PAY-" + System.currentTimeMillis());
        showResult(paymentController.makePayment(request));
    }

    private static void submitChallenge() {
        System.out.println("--- Submit a ticket challenge ---");
        SubmitChallengeRequest request = new SubmitChallengeRequest();
        request.setTicketId(readInt("Ticket id: "));
        request.setReason(readText("Reason for challenge: "));
        showResult(challengeController.submitChallenge(request));
    }

    private static void reviewChallenge() {
        System.out.println("--- Review a challenge ---");
        ReviewChallengeRequest request = new ReviewChallengeRequest();
        request.setChallengeId(readInt("Challenge id: "));
        request.setOfficerId(readInt("Reviewing officer id: "));
        System.out.println("  1. Approve (ticket is waived)");
        System.out.println("  2. Reject");
        int decision = readChoice("Choose decision: ", 2);
        request.setDecisionNotes(readText("Decision notes: "));
        if (decision == 1) {
            showResult(challengeController.approveChallenge(request));
        } else {
            showResult(challengeController.rejectChallenge(request));
        }
    }

    private static void viewSummaries() {
        System.out.println("--- Summaries ---");
        List<?> motorists = listOf(driverController.findAllDriver());
        List<?> officers = listOf(officerController.findAllOfficers());
        List<?> payments = listOf(paymentController.findAllPayments());

        int activeOfficers = 0;
        for (Object officer : officers) {
            if (((FindAllOfficersResponse) officer).isActive()) activeOfficers++;
        }
        BigDecimal totalCollected = BigDecimal.ZERO;
        for (Object payment : payments) {
            totalCollected = totalCollected.add(((FindAllPaymentsResponse) payment).getAmount());
        }

        System.out.println("Registered motorists : " + motorists.size());
        System.out.println("Traffic officers     : " + officers.size() + " (" + activeOfficers + " active)");
        System.out.println("Payments recorded    : " + payments.size());
        System.out.println("Total collected      : N" + totalCollected);
    }

    private static List<?> listOf(ApiResponse response) {
        if (response.isStatus() && response.getData() instanceof List<?> list) return list;
        return List.of();
    }

    private static void showResult(ApiResponse response) {
        if (!response.isStatus()) {
            String message = String.valueOf(response.getData());
            if (response.getData() == null) message = "No records found.";
            System.out.println("Error: " + message);
            return;
        }
        System.out.println("Success:");
        printData(response.getData());
    }

    private static void printData(Object data) {
        if (data instanceof List<?> list) {
            if (list.isEmpty()) System.out.println("  No records found.");
            for (Object item : list) {
                printFields(item);
                System.out.println();
            }
        } else {
            printFields(data);
        }
    }

    private static void printFields(Object data) {
        if (data == null || data instanceof String) {
            System.out.println("  " + data);
            return;
        }
        for (Field field : data.getClass().getDeclaredFields()) {
            if (Modifier.isStatic(field.getModifiers())) continue;
            try {
                field.setAccessible(true);
                Object value = field.get(data);
                System.out.println("  " + label(field.getName()) + ": " + (value == null ? "-" : value));
            } catch (IllegalAccessException e) {
                System.out.println("  " + label(field.getName()) + ": (unavailable)");
            }
        }
    }

    private static String label(String fieldName) {
        String name = fieldName.replaceFirst("^is(?=[A-Z])", "");
        String spaced = name.replaceAll("([A-Z])", " $1").trim().toLowerCase();
        return Character.toUpperCase(spaced.charAt(0)) + spaced.substring(1);
    }

    private static String readText(String prompt) {
        System.out.print(prompt);
        if (!scanner.hasNextLine()) {
            System.out.println();
            System.out.println("Input closed. Goodbye.");
            System.exit(0);
        }
        return scanner.nextLine().trim();
    }

    private static int readInt(String prompt) {
        while (true) {
            String input = readText(prompt);
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a whole number.");
            }
        }
    }

    private static int readChoice(String prompt, int max) {
        while (true) {
            int choice = readInt(prompt);
            if (choice >= 1 && choice <= max) return choice;
            System.out.println("Please choose a number between 1 and " + max + ".");
        }
    }
}