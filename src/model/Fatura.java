package model;
import java.util.ArrayList;
import java.util.List;

public class Fatura {
    //--------------------Atributos--------------------//
    
    private int codigo;
    private Cliente cliente;
    private String dataEmissao;
    private ArrayList<ItemFatura> itens;
    private double valorTotal;
    private Estoque estoque;

    //------------------- Construtor ------------------//
    
    public Fatura(
        int codigo,
        Cliente cliente,
        String dataEmissao,
        Estoque estoque
    ) {
        this.codigo = codigo;
        this.cliente = cliente;
        this.dataEmissao = dataEmissao;
        this.estoque = estoque;
        this.itens = new ArrayList<>();
        this.valorTotal = 0.0;
    }
    
    //--------------------- Métodos -------------------//
    
    private void recalcularValorTotal() {
        valorTotal = 0.0;

        for (ItemFatura item : itens) {
            valorTotal += item.calcularSubtotal();
        }
    }

    private String formatarCampoOpcional (String valor) {
        if (valor == null) {
            return "Não informado";
        }

        return valor;
    }

    public void imprimirFatura() {
        System.out.println("========================================");
        System.out.printf("FATURA Nº %d%n", codigo);
        System.out.printf("Data de emissão: %s%n", dataEmissao);
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

    //--------------------- Getters -------------------//

    private ItemFatura buscarItem(Produto produto){
        for (ItemFatura item : itens){
            if (item.correspondeAo(produto)){
                return item;
            }
        }

        return null;
    }

    public int getCodigo() {
        return codigo;
    }

    public Cliente getCliente() {
        return cliente;
    }
        
    public String getDataEmissao() {
        return dataEmissao;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public List<ItemFatura> getItens() {
        return List.copyOf(itens);
    }

    //--------------------- Setters -------------------//

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