package client;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.OptionalDouble;
import java.util.Random;

import application.AtribuirInstalacaoService;
import business.MockData;
import facade.dto.InstalacaoDTO;
import facade.exceptions.ApplicationException;

public class SimpleClient {

	private static final String DATE_FORMAT = "dd-MMM-yyyy HH:mm:ss";

	public static void main(String[] args) {

		//BilGes app = new Bilges();

		try {
			app.run();

			// Access both available services
			CriarEventoService ces = app.getCriarEventoService();
			AtribuirInstalacaoService ais = app.getAtribuirInstalacaoService();
			ComprarBilheteService cbs = app.getComprarBilheteService();

			// teste 1
			System.out.println(ces.pedirTipoEvento());

			List<String> listDatas = new ArrayList<>();
			listDatas.add("09-May-2021 21:00:00");
			listDatas.add("09-May-2021 24:00:00");
			List<Date> datasHoras = defineData(listDatas);
			ces.criarEvento("TeteATete", "Bye Semestre X", datasHoras, 1);


			//teste 2
			List<String> listDatas2 = new ArrayList<>();
			listDatas2.add("09-May-2021 20:00:00");
			listDatas2.add("09-May-2021 22:00:00");
			List<Date> datasHoras2 = defineData(listDatas2);
			ces.criarEvento("TeteATete", "Bye Semestre Y", datasHoras2, 1);

			//teste 3
			List<String> listDatas3 = new ArrayList<>();
			listDatas3.add("17-Jul-2021 21:00:00");
			listDatas3.add("17-Jul-2021 23:30:00");
			listDatas3.add("18-Jul-2021 15:00:00");
			listDatas3.add("18-Jul-2021 20:00:00");
			List<Date> datasHoras3 = defineData(listDatas3);
			ces.criarEvento("BandoSentado", "Open dos Exames", datasHoras3, 1);

			//teste 4 insucesso -> a produtora não está certificada para o tipo de evento
			List<String> listDatas4 = new ArrayList<>();
			listDatas4.add("31-Jul-2021 21:00:00");
			listDatas4.add("31-Jul-2021 23:00:00");
			listDatas4.add("1-Aug-2021 14:00:00");
			listDatas4.add("1-Aug-2021 19:00:00");
			List<Date> datasHoras4 = defineData(listDatas4);
			ces.criarEvento("MultidaoEmPe", "Festival Estou de Ferias", datasHoras4, 1);

			//teste 5 insucesso -> datas não são consecutivas
			List<String> listDatas5 = new ArrayList<>();
			listDatas5.add("31-Dec-2021 21:00:00");
			listDatas5.add("31-Dec-2021 23:00:00");
			listDatas5.add("1-Aug-2021 14:00:00");
			listDatas5.add("1-Aug-2021 19:00:00");
			List<Date> datasHoras5 = defineData(listDatas5);
			ces.criarEvento("MultidaoEmPe", "Festival Estou de Ferias", datasHoras5, 2);

			//teste 6
			List<String> listDatas6 = new ArrayList<>();
			listDatas6.add("31-Jul-2021 21:00:00");
			listDatas6.add("31-Jul-2021 23:00:00");
			listDatas6.add("1-Aug-2021 14:00:00");
			listDatas6.add("1-Aug-2021 19:00:00");
			List<Date> datasHoras6 = defineData(listDatas6);
			ces.criarEvento("MultidaoEmPe", "Festival Estou de Ferias", datasHoras6, 2);// teste 1
			System.out.println(ces.pedirTipoEvento());

			List<String> listDatas = new ArrayList<>();
			listDatas.add("09-May-2021 21:00:00");
			listDatas.add("09-May-2021 24:00:00");
			List<Date> datasHoras = defineData(listDatas);
			ces.criarEvento("TeteATete", "Bye Semestre X", datasHoras, 1);

			//obter instalacoes

			Iterable<InstalacaoDTO> listaInstalacoes = ais.atribuirInstalacao();

			System.out.println("Lista de instalacoes disponiveis:");

			for(InstalacaoDTO s: listaInstalacoes) {
				System.out.println(s.toString());
			}

			//teste 7
			ais.darInfoAtribuicao("Bye Semestre X", "Mini Estadio", MockData.getDataAtual(), 15.00, null);

			//teste 8
			ais.darInfoAtribuicao("Bye Semestre X", "Micro Pavilhao", MockData.getDataAtual(), 20.00, null);

			//teste 9
			ais.darInfoAtribuicao("Bye Semestre X", "Micro Pavilhao", MockData.getDataAtual(), 20.00, null);

			//teste 10
			OptionalDouble precoPasse = OptionalDouble.of(30);
			ais.darInfoAtribuicao("Open dos Exames", "Mini Estadio", MockData.getDataAtual(), 20.00, precoPasse);

			//teste 11
			ais.darInfoAtribuicao("Festival Estou de Ferias", "Pequeno Relvado", MockData.getDataAtual(), 15.00, null);

			Random rand = new Random();

			//teste 12
			List<Date> datas12 = cbs.comprarBilhetesLugares("Bye Semestre X");

			List<String> lugares12 = cbs.escolherData(datas12.get(rand.nextInt(datas12.size())));

			List<String> tresPrimeirosLugares = new ArrayList<>();

			for(int i = 0; i<3; i++) {
				tresPrimeirosLugares.add(lugares12.get(i));
			}

			System.out.print(cbs.escolhaLugares(tresPrimeirosLugares, "u1@gmail.com"));

			//teste 13
			List<String> lugares13 = new ArrayList<>();
			lugares13.add(tresPrimeirosLugares.get(2));
			System.out.print(cbs.escolhaLugares(lugares13, "u2@gmail.com"));

			//teste 14
			List<String> lugares3 = new ArrayList<>();
			lugares3.add("B-2");
			System.out.println(cbs.escolhaLugares(lugares3, "u2@gmail.com"));

			//teste 15 insucesso -> o evento não tem lugares marcados
			List<Date> list1 = cbs.comprarBilhetesLugares("Festival Estou de Ferias");

			//teste 16
			List<Date> list2 = cbs.comprarBilhetesLugares("Open dos Exames");
			String sdata2 = "17-Jul-2021 21:00:00";
			Date data2 = new SimpleDateFormat(DATE_FORMAT).parse(sdata2);
			List<String> listLug = cbs.escolherData(data2);
			List<String> lugares4 = new ArrayList<>();
			lugares4.add("A-1");
			lugares4.add("A-2");
			System.out.println(cbs.escolhaLugares(lugares4, "u2@gmail.com"));

			//teste 17
			int numBilhetesPasse17 = cbs.comprarBilhetesPasse("Open dos Exames");


			int numeroCompra17 = rand.nextInt(numBilhetesPasse17 + 1);

			System.out.println(cbs.escolhaNumBilhetes(numeroCompra17, "u4@gmail.com"));

			//teste 18
			int numBilhetesPasse18 = cbs.comprarBilhetesPasse("Open dos Exames");

			int numeroCompra18 = rand.nextInt(numBilhetesPasse18 + 1);

			System.out.println(cbs.escolhaNumBilhetes(numeroCompra18, "u4@gmail.com"));

			//teste 20
			int numBilhetesPasse20 = cbs.comprarBilhetesPasse("Open dos Exames");

			int numeroCompra20 = rand.nextInt(numBilhetesPasse20 + 1);

			System.out.println(cbs.escolhaNumBilhetes(numeroCompra20, "u4@gmail.com"));




			app.stopRun();
		} catch (ApplicationException e) {
			System.out.println("Error: " + e.getMessage());
			// for debugging purposes only. Typically, in the application
			// this information can be associated with a "details" button when
			// the error message is displayed.
			if (e.getCause() != null)
				System.out.println("Cause: ");
			e.printStackTrace();
		}
	}

	private static List<Date> defineData(List<String> listDatas) {
		List<Date> datasHoras = new ArrayList<>();

		for (String sdata : listDatas) {
			try {
				Date data = new SimpleDateFormat(DATE_FORMAT).parse(sdata);
				datasHoras.add(data);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return datasHoras;
	}

}