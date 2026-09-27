import model.*;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class TestesSistema {

    @Test
    void deveRejeitarValorDeProdutoInvalido() {
        assertThrows(
            IllegalArgumentException.class,
            () -> new Produto(
                1,
                "Produto inválido",
                "12345678",
                0,
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
            8.00,
            5
        );

        estoque.cadastrarProduto(
            "coca-cola",
            "22021000",
            8.00,
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
        assertEquals(24.00, fatura.getValorTotal(), 0.001);
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
        assertEquals(0.0, fatura.getValorTotal(), 0.001);
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

    private Estoque criarEstoque() {
        Estoque estoque = new Estoque();

        estoque.cadastrarProduto(
            "Coca-Cola",
            "22021000",
            8.00,
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
            "25/09/2026",
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
            "Coca-Cola", "22021000", 8.00, 5
        );

        estoque.cadastrarProduto(
            "coca-cola", "22021000", 8.00, 3
        );

        estoque.cadastrarProduto(
            "Guaraná", "22021000", 7.00, 10
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
        assertEquals(24.00, fatura.getValorTotal(), 0.001);
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
        assertEquals(16.00, fatura.getValorTotal(), 0.001);
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
}