package client;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import business.util.Triplet;

import business.event.exceptions.DateInvalidException;
import business.producer.exceptions.ProducerWithoutCertification;
import facade.exceptions.ApplicationException;
import facade.services.CreateEventService;

public class SimpleClientCreateEvent {

	private CreateEventService ces;

	private static final String SEPARATOR = "################################################";
	private static final String EXPECTED = "Expected: event created without error";
	private static final String RESULT_PASSED = "Result: passed!";
	private static final String RESULT_ERROR = "Result: failed!";
	private static final String CAUSE = "Cause: ";
	private static final String TEST = "\nStart of the Test!";

	protected SimpleClientCreateEvent(CreateEventService ces) {
		this.ces = ces;
	}

	protected void run() {

		Logger logger = Logger.getLogger(SimpleClient.class.getName());

		/*
		 * 1. Criar evento com os seguintes dados: tipo de evento: TeteATete nome: Bye
		 * Semestre X datas: 9/5/2021 hora início: 21:00 hora fim: 24:00 produtora: 1
		 */
		System.out.println(SEPARATOR);
		System.out.println("Test 1: create event Bye Semestre X");
		System.out.println(EXPECTED);
		try {
			System.out.println(TEST);
			List<String> list = ces.createEvent();

			System.out.print("Event Types are : ");
			for (String string : list) {
				if (list.get(list.size() - 1).equals(string)) {
					System.out.print(" " + string + "\n");
				} else
					System.out.print(" " + string + ",");
			}

			LocalDate ld = LocalDate.of(2021, 5, 9);
			LocalTime ltStart = LocalTime.of(21, 0);
			LocalTime ltEnding = LocalTime.of(0, 0);
			Triplet<LocalDate, LocalTime, LocalTime> triple = new Triplet<>(ld, ltStart, ltEnding);
			List<Triplet<LocalDate, LocalTime, LocalTime>> test = new ArrayList<>();
			test.add(triple);
			ces.createEventWithInfo("TeteATete", "Bye Semestre X", test, 1);
			System.out.println(RESULT_PASSED);
		} catch (ApplicationException e) {

			logger.log(Level.WARNING, String.format("Error: %s", e.getMessage()));

			if (e.getCause() != null)
				logger.log(Level.INFO, CAUSE);
			e.printStackTrace();
			System.out.println(RESULT_ERROR);
			System.exit(1);
		}

		/*
		 * 2. Criar evento com os seguintes dados: tipo de evento: TeteATete nome: Bye
		 * Semestre Y datas: 9/5/2021 hora início: 20:00 hora fim: 22:00 produtora: 1
		 */
		System.out.println(SEPARATOR);
		System.out.println("Test 2: create event: Bye Semestre Y");
		System.out.println(EXPECTED);
		try {
			System.out.println(TEST);
			List<String> list = ces.createEvent();

			System.out.print("Event Types are : ");
			for (String string : list) {
				if (list.get(list.size() - 1).equals(string)) {
					System.out.print(" " + string + "\n");
				} else
					System.out.print(" " + string + ",");
			}

			LocalDate ld = LocalDate.of(2021, 5, 9);
			LocalTime ltStart = LocalTime.of(20, 0);
			LocalTime ltEnding = LocalTime.of(22, 0);
			Triplet<LocalDate, LocalTime, LocalTime> triple = new Triplet<>(ld, ltStart, ltEnding);
			List<Triplet<LocalDate, LocalTime, LocalTime>> test = new ArrayList<>();
			test.add(triple);
			ces.createEventWithInfo("TeteATete", "Bye Semestre Y", test, 1);
			System.out.println(RESULT_PASSED);

		} catch (ApplicationException e) {

			logger.log(Level.WARNING, String.format("Error: %s", e.getMessage()));

			if (e.getCause() != null)
				logger.log(Level.INFO, CAUSE);
			e.printStackTrace();
			System.out.println(RESULT_ERROR);
			System.exit(1);
		}

		/*
		 * 3. Criar evento com os seguintes dados: tipo de evento: BandoSentado nome:
		 * Open dos Exames datas: 17/7/2021 hora início: 21:00 hora fim: 23:30 datas:
		 * 18/7/2021 hora início: 15:00 hora fim: 20:00 produtora: 1
		 */
		System.out.println(SEPARATOR);
		System.out.println("Test 3: create event: Open dos Exames");
		System.out.println(EXPECTED);
		try {
			System.out.println(TEST);
			List<String> list = ces.createEvent();

			System.out.print("Event Types are : ");
			for (String string : list) {
				if (list.get(list.size() - 1).equals(string)) {
					System.out.print(" " + string + "\n");
				} else
					System.out.print(" " + string + ",");
			}
			// first date
			LocalDate ld = LocalDate.of(2021, 7, 17);
			LocalTime ltStart = LocalTime.of(21, 0);
			LocalTime ltEnding = LocalTime.of(23, 30);

			// second date

			LocalDate ld2 = LocalDate.of(2021, 7, 18);
			LocalTime ltStart2 = LocalTime.of(15, 0);
			LocalTime ltEnding2 = LocalTime.of(20, 0);

			Triplet<LocalDate, LocalTime, LocalTime> triple = new Triplet<>(ld, ltStart, ltEnding);
			Triplet<LocalDate, LocalTime, LocalTime> triple2 = new Triplet<>(ld2, ltStart2, ltEnding2);
			List<Triplet<LocalDate, LocalTime, LocalTime>> test = new ArrayList<>();
			test.add(triple);
			test.add(triple2);

			ces.createEventWithInfo("BandoSentado", "Open dos Exames", test, 1);
			System.out.println(RESULT_PASSED);
		} catch (ApplicationException e) {

			logger.log(Level.WARNING, String.format("Error: %s", e.getMessage()));

			if (e.getCause() != null)
				logger.log(Level.INFO, CAUSE);
			e.printStackTrace();
			System.out.println(RESULT_ERROR);
			System.exit(1);
		}

		/*
		 * 4. Criar evento com os seguintes dados: tipo de evento: MultidaoEmPe nome:
		 * Festival Estou de Ferias datas: 31/7/2021 hora início: 21:00 hora fim: 23:00
		 * datas: 1/8/2021 hora início: 14:00 hora fim: 19:00 produtora: 1 > a produtora
		 * não está certificada para o tipo de evento
		 */
		System.out.println(SEPARATOR);
		System.out.println("Test 4: create event: Festival Estou de Ferias");
		System.out.println("Expected: error -> producer is not certified for this event type");
		try {
			System.out.println(TEST);
			List<String> list = ces.createEvent();

			System.out.print("Event Types are : ");
			for (String string : list) {
				if (list.get(list.size() - 1).equals(string)) {
					System.out.print(" " + string + "\n");
				} else
					System.out.print(" " + string + ",");
			}

			// first date
			LocalDate ld = LocalDate.of(2021, 7, 31);
			LocalTime ltStart = LocalTime.of(21, 0);
			LocalTime ltEnding = LocalTime.of(23, 0);

			// second date

			LocalDate ld2 = LocalDate.of(2021, 8, 1);
			LocalTime ltStart2 = LocalTime.of(14, 0);
			LocalTime ltEnding2 = LocalTime.of(19, 0);

			Triplet<LocalDate, LocalTime, LocalTime> triple = new Triplet<>(ld, ltStart, ltEnding);
			Triplet<LocalDate, LocalTime, LocalTime> triple2 = new Triplet<>(ld2, ltStart2, ltEnding2);
			List<Triplet<LocalDate, LocalTime, LocalTime>> test = new ArrayList<>();
			test.add(triple);
			test.add(triple2);

			ces.createEventWithInfo("MultidaoEmPe", "Festival Estou de Ferias", test, 1);
			System.out.println(RESULT_ERROR);
			System.exit(1);
		} catch (ApplicationException e) {
			if (e.getCause().getClass().equals(ProducerWithoutCertification.class)) {
				System.out.println(RESULT_PASSED);
			} else {
				logger.log(Level.WARNING, String.format("Error: %s", e.getMessage()));

				if (e.getCause() != null)
					logger.log(Level.INFO, CAUSE);
				e.printStackTrace();
				System.exit(1);
			}
		}

		/*
		 * 5. Criar evento com os seguintes dados: tipo de evento: MultidaoEmPe nome:
		 * Festival Estou de Ferias datas: 31/12/2021 hora início: 21:00 hora fim: 23:00
		 * datas: 1/8/2021 hora início: 14:00 hora fim: 19:00 produtora: 2 > datas não
		 * são consecutivas
		 */
		System.out.println(SEPARATOR);
		System.out.println("Test 5: create event: Festival Estou de Ferias");
		System.out.println("Expected: error -> dates are not consecutive");
		try {
			System.out.println(TEST);
			List<String> list = ces.createEvent();

			System.out.print("Event Types are : ");
			for (String string : list) {
				if (list.get(list.size() - 1).equals(string)) {
					System.out.print(" " + string + "\n");
				} else
					System.out.print(" " + string + ",");
			}

			// first date
			LocalDate ld = LocalDate.of(2021, 12, 31);
			LocalTime ltStart = LocalTime.of(21, 0);
			LocalTime ltEnding = LocalTime.of(23, 0);

			// second date

			LocalDate ld2 = LocalDate.of(2021, 8, 1);
			LocalTime ltStart2 = LocalTime.of(14, 0);
			LocalTime ltEnding2 = LocalTime.of(19, 0);

			Triplet<LocalDate, LocalTime, LocalTime> triple = new Triplet<>(ld, ltStart, ltEnding);
			Triplet<LocalDate, LocalTime, LocalTime> triple2 = new Triplet<>(ld2, ltStart2, ltEnding2);
			List<Triplet<LocalDate, LocalTime, LocalTime>> test = new ArrayList<>();
			test.add(triple);
			test.add(triple2);

			ces.createEventWithInfo("MultidaoEmPe", "Festival Estou de Ferias", test, 2);
			System.out.println(RESULT_ERROR);
			System.exit(1);
		} catch (ApplicationException e) {
			if (e.getCause().getClass().equals(DateInvalidException.class)) {
				System.out.println(RESULT_PASSED);
			} else {
				logger.log(Level.WARNING, String.format("Error: %s", e.getMessage()));

				if (e.getCause() != null)
					logger.log(Level.INFO, CAUSE);
				e.printStackTrace();
				System.exit(1);
			}
		}

		/*
		 * 6. Criar evento com os seguintes dados: tipo de evento: MultidaoEmPe nome:
		 * Festival Estou de Ferias datas: 31/7/2021 hora início: 21:00 hora fim: 23:00
		 * datas: 1/8/2021 hora início: 14:00 hora fim: 19:00 produtora: 2
		 */
		System.out.println(SEPARATOR);
		System.out.println("Test 6: create event: Festival Estou de Ferias");
		System.out.println(EXPECTED);
		try {
			System.out.println(TEST);
			List<String> list = ces.createEvent();

			System.out.print("Event Types are : ");
			for (String string : list) {
				if (list.get(list.size() - 1).equals(string)) {
					System.out.print(" " + string + "\n");
				} else
					System.out.print(" " + string + ",");
			}

			// first date
			LocalDate ld = LocalDate.of(2021, 07, 31);
			LocalTime ltStart = LocalTime.of(21, 0);
			LocalTime ltEnding = LocalTime.of(23, 0);

			// second date

			LocalDate ld2 = LocalDate.of(2021, 8, 1);
			LocalTime ltStart2 = LocalTime.of(14, 0);
			LocalTime ltEnding2 = LocalTime.of(19, 0);

			Triplet<LocalDate, LocalTime, LocalTime> triple = new Triplet<>(ld, ltStart, ltEnding);
			Triplet<LocalDate, LocalTime, LocalTime> triple2 = new Triplet<>(ld2, ltStart2, ltEnding2);
			List<Triplet<LocalDate, LocalTime, LocalTime>> test = new ArrayList<>();
			test.add(triple);
			test.add(triple2);

			ces.createEventWithInfo("MultidaoEmPe", "Festival Estou de Ferias", test, 2);
			System.out.println(RESULT_PASSED);
		} catch (ApplicationException e) {
			logger.log(Level.WARNING, String.format("Error: %s", e.getMessage()));

			if (e.getCause() != null)
				logger.log(Level.INFO, CAUSE);
			e.printStackTrace();
			System.out.println(RESULT_ERROR);
			System.exit(1);
		}

	}

}
