import model.*;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;

// Verifica as regras atuais de cliente, produto, estoque e fatura. //
public class TestesSistema {

    @Test
    void deveRejeitarValorDeProdutoInvalido() {
        assertThrows(
            IllegalArgumentException.class,
            () -> new Produto(
                1,
                "Produto inválido",
                "12345678",
                "nada a ver",
                BigDecimal.ZERO,
                1
            )
        );
    }

    @Test
    void deveAgruparProdutosComMesmoNome() {
        Estoque estoque = new Estoque();

        estoque.cadastrarProduto(
            "Coca-Cola",
            "22021000",
            "Coca-Cola_company",
            new BigDecimal("8"),
            5
        );

        estoque.cadastrarProduto(
            "coca-cola",
            "22021000",
            "Coca-Cola_company",
            new BigDecimal("8"),
            3
        );

        assertEquals(
            1,
            estoque.getQuantidadeDeProdutosDiferentes()
        );

        assertEquals(
            8,
            estoque.getQuantidadeTotalEmEstoque()
        );
    }

    @Test
    void deveRetirarProdutosAoAdicionarNaFatura() {
        Estoque estoque = criarEstoque();
        Produto produto = estoque.getProdutos().get(0);
        Fatura fatura = criarFatura(estoque);

        fatura.adicionarProduto(produto, 3);

        assertEquals(2, estoque.getQuantidadeTotalEmEstoque());
        assertEquals(0, new BigDecimal("24").compareTo(fatura.getValorTotal()));
        assertEquals(1, fatura.getItens().size());
    }

    @Test
    void deveDevolverProdutosAoRemoverDaFatura() {
        Estoque estoque = criarEstoque();
        Produto produto = estoque.getProdutos().get(0);
        Fatura fatura = criarFatura(estoque);

        fatura.adicionarProduto(produto, 3);
        fatura.removerProduto(produto, 3);

        assertEquals(5, estoque.getQuantidadeTotalEmEstoque());
        assertEquals(0, new BigDecimal("0").compareTo(fatura.getValorTotal()));
        assertTrue(fatura.getItens().isEmpty());
    }

    @Test
    void deveRejeitarQuantidadeMaiorQueOEstoque() {
        Estoque estoque = criarEstoque();
        Produto produto = estoque.getProdutos().get(0);
        Fatura fatura = criarFatura(estoque);

        assertThrows(
            IllegalArgumentException.class,
            () -> fatura.adicionarProduto(produto, 6)
        );

        assertEquals(5, estoque.getQuantidadeTotalEmEstoque());
        assertTrue(fatura.getItens().isEmpty());
    }

    // Dados de apoio compartilhados mantêm os cenários de teste mais concisos. //
    private Estoque criarEstoque() {
        Estoque estoque = new Estoque();

        estoque.cadastrarProduto(
            "Coca-Cola",
            "22021000",
            "Coca-Cola_company",
            new BigDecimal("8"),
            5
        );

        return estoque;
    }

    private Fatura criarFatura(Estoque estoque) {
        Cliente cliente = new Cliente(
            "Empresa Exemplo Ltda.",
            "04.252.011/0001-10",
            "123456789",
            "Rua das Flores, 100",
            "contato@empresa.com",
            null
        );

        return new Fatura(
            1,
            cliente,
            LocalDate.of(2026, 9, 25),
            estoque
        );
    }

    @Test
    void deveFormatarCnpjCorretamente() {
        Cnpj cnpj = new Cnpj("04252011000110");

        assertEquals(
            "04.252.011/0001-10",
            cnpj.formatado()
        );
    }

    @Test
    void deveRejeitarCnpjInvalido() {
        assertThrows(
            IllegalArgumentException.class,
            () -> new Cnpj("04.252.011/0001-99")
        );
    }

    @Test
    void deveExigirRazaoSocial() {
        assertThrows(
            IllegalArgumentException.class,
            () -> new Cliente(
                " ",
                "04.252.011/0001-10",
                null,
                null,
                "cliente@teste.com",
                null
            )
        );
    }

    @Test
    void deveExigirPeloMenosUmContato() {
        assertThrows(
            IllegalArgumentException.class,
            () -> new Cliente(
                "Empresa de Teste",
                "04.252.011/0001-10",
                null,
                null,
                null,
                null
            )
        );
    }

    @Test
    void produtoRepetidoNaoDeveConsumirNovoCodigo() {
        Estoque estoque = new Estoque();

        estoque.cadastrarProduto(
            "Coca-Cola", "22021000", "Coca-Cola_company", new BigDecimal("8"), 5
        );

        estoque.cadastrarProduto(
            "coca-cola", "22021000", "Coca-Cola_company", new BigDecimal("8"), 3
        );

        estoque.cadastrarProduto(
            "Guaraná", "22021000", "Coca-Cola_company", new BigDecimal("7"), 10
        );

        Produto cocaCola = estoque.getProdutos().get(0);
        Produto guarana = estoque.getProdutos().get(1);

        assertEquals(1, cocaCola.getCodigo());
        assertEquals(2, guarana.getCodigo());
        assertEquals(2, estoque.getQuantidadeDeProdutosDiferentes());
        assertEquals(18, estoque.getQuantidadeTotalEmEstoque());
    }

    @Test
    void deveAgruparMesmoProdutoDentroDaFatura() {
        Estoque estoque = criarEstoque();
        Produto produto = estoque.getProdutos().get(0);
        Fatura fatura = criarFatura(estoque);

        fatura.adicionarProduto(produto, 2);
        fatura.adicionarProduto(produto, 1);

        assertEquals(1, fatura.getItens().size());
        assertEquals(3, fatura.getItens().get(0).getQuantidade());
        assertEquals(0, new BigDecimal("24").compareTo(fatura.getValorTotal()));
        assertEquals(2, estoque.getQuantidadeTotalEmEstoque());
    }

    @Test
    void deveRemoverQuantidadeParcialDaFatura() {
        Estoque estoque = criarEstoque();
        Produto produto = estoque.getProdutos().get(0);
        Fatura fatura = criarFatura(estoque);

        fatura.adicionarProduto(produto, 4);

        boolean removido =
            fatura.removerProduto(produto, 2);

        assertTrue(removido);
        assertEquals(1, fatura.getItens().size());
        assertEquals(2, fatura.getItens().get(0).getQuantidade());
        assertEquals(0, new BigDecimal("16.00").compareTo(fatura.getValorTotal()));
        assertEquals(3, estoque.getQuantidadeTotalEmEstoque());
    }

    @Test
    void deveRejeitarRemocaoMaiorQueQuantidadeFaturada() {
        Estoque estoque = criarEstoque();
        Produto produto = estoque.getProdutos().get(0);
        Fatura fatura = criarFatura(estoque);

        fatura.adicionarProduto(produto, 2);

        assertThrows(
            IllegalArgumentException.class,
            () -> fatura.removerProduto(produto, 3)
        );

        assertEquals(2, fatura.getItens().get(0).getQuantidade());
        assertEquals(3, estoque.getQuantidadeTotalEmEstoque());
    }

    @Test
    void copiaDoProdutoNaoDeveAlterarEstoqueReal() {
        Estoque estoque = criarEstoque();

        Produto copia = estoque.getProdutos().get(0);
        copia.setQuantidade(1000);

        Produto produtoReal =
            estoque.getProdutos().get(0);

        assertEquals(5, produtoReal.getQuantidade());
    }

    @Test
    void devePermitirConsultarProdutoComEstoqueZerado() {
        Estoque estoque = criarEstoque();
        Produto produto = estoque.getProdutos().get(0);
        Fatura fatura = criarFatura(estoque);

        fatura.adicionarProduto(produto, 5);

        Produto produtoConsultado =
            estoque.buscarProdutoPorCodigo(produto.getCodigo());

        assertEquals(0, produtoConsultado.getQuantidade());
        assertEquals(0, estoque.getQuantidadeTotalEmEstoque());
        assertEquals(1, estoque.getProdutos().size());
        assertEquals(0, estoque.getProdutos().get(0).getQuantidade());
        assertEquals(0, new BigDecimal("40.00").compareTo(fatura.getValorTotal()));
    }

    @Test
    void devePermitirReporProdutoComEstoqueZerado() {
        Estoque estoque = criarEstoque();
        Produto produto = estoque.getProdutos().get(0);
        Fatura fatura = criarFatura(estoque);

        fatura.adicionarProduto(produto, 5);

        estoque.adicionarQuantidade(produto.getNome(), produto.getMarca(), 3);

        Produto produtoAtualizado =
            estoque.buscarProdutoPorCodigo(produto.getCodigo());

        assertEquals(3, produtoAtualizado.getQuantidade());
        assertEquals(3, estoque.getQuantidadeTotalEmEstoque());
        assertEquals(1, estoque.getQuantidadeDeProdutosDiferentes());

        assertEquals(5, fatura.getItens().get(0).getQuantidade());
        assertEquals(0, new BigDecimal("40.00").compareTo(fatura.getValorTotal()));
    }

    @Test
    void deveRejeitarSaldoInicialNegativo() {
        assertThrows(
            IllegalArgumentException.class,
            () -> new Produto(
                1, "Produto de teste", "12345678", "Coca-Cola_company", new BigDecimal("8"), -1
            )
        );
    }

    @Test
    void deveRejeitarSaldoNegativoSemAlterarSaldoAtual() {
        Produto produto = new Produto(
            1, "Produto de teste", "12345678", "Coca-Cola_company", new BigDecimal("8"), 5
        );

        assertThrows(
            IllegalArgumentException.class,
            () -> produto.setQuantidade(-1)
        );

        assertEquals(5, produto.getQuantidade());
    }

    @Test
    void deveRejeitarMovimentacoesComQuantidadeInvalida() {
        Produto produto = new Produto(
            1, "Produto de teste", "12345678", "Coca-Cola_company", new BigDecimal("8"), 5
        );

        assertThrows(
            IllegalArgumentException.class,
            () -> produto.adicionarQuantidade(0)
        );

        assertThrows(
            IllegalArgumentException.class,
            () -> produto.adicionarQuantidade(-1)
        );

        assertThrows(
            IllegalArgumentException.class,
            () -> produto.removerQuantidade(0)
        );

        assertThrows(
            IllegalArgumentException.class,
            () -> produto.removerQuantidade(-1)
        );

        assertEquals(5, produto.getQuantidade());
    }
}
