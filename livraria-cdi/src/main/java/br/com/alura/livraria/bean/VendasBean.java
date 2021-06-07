package br.com.alura.livraria.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;

import br.com.alura.alura_lib.dao.DAO;
import br.com.alura.alura_lib.jsf.annotation.ViewModel;
import br.com.alura.livraria.modelo.Livro;
import br.com.alura.livraria.modelo.Venda;

/*
 * @ViewModel e' um Stereotype (esteriotipo) definido por mim, para as anotacoes @Named e @ViewScoped.
 * Ou seja, usar estas duas ultimas anotacoes e' a mesma coisa que usar a anotacao @ViewModel
 * 
 * @ViewModel = @Named + @ViewScoped (javax.faces.view.ViewScoped)
 * 
 */

// @Named
// @ViewScoped //javax.faces.view.ViewScoped

@ViewModel
public class VendasBean implements Serializable {


	private static final long serialVersionUID = 1L;
	
	private DAO<Livro, Integer> livroDao;

	@Inject
	public VendasBean(DAO<Livro, Integer> livroDao) {
		this.livroDao = livroDao;
	}
	
	public BarChartModel getVendasModel() {

		BarChartModel model = new BarChartModel();
		model.setAnimate(true);
		
		ChartSeries vendaSerie = new ChartSeries();
		vendaSerie.setLabel("Vendas 2016");

		List<Venda> vendas = getVendas(1234);
		for (Venda venda : vendas) {
			vendaSerie.set(venda.getLivro().getTitulo(), venda.getQuantidade());
		}
		
		ChartSeries vendaSerie2015 = new ChartSeries();
		vendaSerie2015.setLabel("Vendas 2015");
		
		vendas = getVendas(4321);
		for (Venda venda : vendas) {
			vendaSerie2015.set(venda.getLivro().getTitulo(), venda.getQuantidade());
		}

		model.addSeries(vendaSerie);
		model.addSeries(vendaSerie2015);

		return model;
	}

	public List<Venda> getVendas(long seed) {

		List<Livro> livros = livroDao.listaTodos();
		List<Venda> vendas = new ArrayList<Venda>();

		Random random = new Random(seed);

		for (Livro livro : livros) {
			Integer quantidade = random.nextInt(500);
			vendas.add(new Venda(livro, quantidade));
		}

		return vendas;
	}
}
