package model;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

// Representa uma fatura e coordena seus itens com as movimentações do estoque. //
public class Fatura {
    // Atributos //
    
    private int codigo;
    private Cliente cliente;
    private LocalDate dataEmissao;
    private ArrayList<ItemFatura> itens;
    private BigDecimal valorTotal;
    private Estoque estoque;

    // Métodos de acesso //

    // Retorna o código identificador da fatura. //
    public int getCodigo() {
        return codigo;
    }

    // Retorna o cliente associado à fatura. //
    public Cliente getCliente() {
        return cliente;
    }
        
    // Retorna a data de emissão informada para a fatura. //
    public LocalDate getDataEmissao() {
        return dataEmissao;
    }

    // Retorna o valor total atualizado dos itens da fatura. //
    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    // Retorna uma visão imutável dos itens atualmente faturados. //
    public List<ItemFatura> getItens() {
        return List.copyOf(itens);
    }

    // Construtor //
    
    // Cria uma fatura vazia vinculada ao cliente e ao estoque. //
    public Fatura(
        int codigo,
        Cliente cliente,
        LocalDate dataEmissao,
        Estoque estoque
    ) {
        this.codigo = codigo;
        this.cliente = cliente;
        this.dataEmissao = dataEmissao;
        this.estoque = estoque;
        this.itens = new ArrayList<>();
        this.valorTotal = BigDecimal.ZERO;
    }
    
    // Apresentação //
    
    private void recalcularValorTotal() {
        valorTotal = BigDecimal.ZERO;

        for (ItemFatura item : itens) {
            valorTotal = valorTotal.add(item.calcularSubtotal());
        }
    }

    private String formatarCampoOpcional (String valor) {
        if (valor == null) {
            return "Não informado";
        }

        return valor;
    }

    // Exibe os dados do cliente, os itens e o valor total da fatura. //
    public void imprimirFatura() {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/uuuu");
        System.out.println("========================================");
        System.out.printf("FATURA Nº %d%n", codigo);
        System.out.printf("Data de emissão: %s%n", 
            dataEmissao.format(formato)
        );
        System.out.println("========================================");

        System.out.println("DADOS DO CLIENTE");
        System.out.printf(
            "Razão social: %s%n",
            cliente.getRazaoSocial()
        );
        System.out.printf(
            "CNPJ: %s%n",
            cliente.getCnpj()
        );
        System.out.printf(
            "Inscrição estadual: %s%n",
            formatarCampoOpcional(cliente.getInscricaoEstadual())
        );
        System.out.printf(
            "Endereço: %s%n",
            formatarCampoOpcional(cliente.getEndereco())
        );
        System.out.printf(
            "E-mail: %s%n",
            formatarCampoOpcional(cliente.getEmail())
        );
        System.out.printf(
            "Celular: %s%n",
            formatarCampoOpcional(cliente.getCelular())
        );

        System.out.println("========================================");
        System.out.println("PRODUTOS");

        for (ItemFatura item : itens) {
            Produto produto = item.getProduto();

            System.out.printf(
                "%-20.20s | NCM: %-8.8s | Qtd.: %3d | Unitário: R$ %8.2f | Subtotal: R$ %8.2f%n",
                produto.getNome(),
                produto.getNcm(),
                item.getQuantidade(),
                produto.getValor(),
                item.calcularSubtotal()
            );
        }

        System.out.println("========================================");
        System.out.printf(
            "VALOR TOTAL: R$ %.2f%n",
            valorTotal
        );
        System.out.println("========================================");
    }

    // Consultas //

    // Localiza a linha correspondente para agrupar o mesmo produto na fatura. //
    private ItemFatura buscarItem(Produto produto){
        for (ItemFatura item : itens){
            if (item.correspondeAo(produto)){
                return item;
            }
        }

        return null;
    }

    // Operações //

    // Adiciona unidades à fatura e as retira do estoque. //
    public void adicionarProduto(
        Produto produto,
        int quantidade
    ) {
        estoque.retirarProduto(produto, quantidade);

        ItemFatura item = buscarItem(produto);

        if (item == null) {
            itens.add(new ItemFatura(produto, quantidade));
        } else {
            item.adicionarQuantidade(quantidade);
        }

        recalcularValorTotal();
    }

    // Remove unidades da fatura, devolve-as ao estoque e atualiza o total. //
    public boolean removerProduto(
            Produto produto,
            int quantidade
    ) {
        ItemFatura item = buscarItem(produto);

        if (item == null) {
            return false;
        }

        item.removerQuantidade(quantidade);

        estoque.devolverProduto(
            produto,
            quantidade
        );

        if (item.estaVazio()) {
            itens.remove(item);
        }

        recalcularValorTotal();

        return true;
    }

}
