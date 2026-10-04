package model;

// Representa os dados cadastrais e as formas de contato de um cliente. //
public class Cliente {
    // Atributos //
    
    private String razaoSocial;
    private Cnpj cnpj;
    private String inscricaoEstadual;
    private String endereco;
    private String email;
    private String celular;
    
    // Métodos de alteração //

    // Atualiza a razão social após validar que não está vazia. //
    public void setRazaoSocial(String razaoSocial) {
        validarTextoObrigatorio(
            razaoSocial,
            "A razão social"
        );

        this.razaoSocial = razaoSocial.trim();
    }
    
    // Cria e armazena um CNPJ validado a partir do texto informado. //
    public void setCnpj(String cnpj) {
        this.cnpj = new Cnpj(cnpj);
    }

    // Normaliza a inscrição estadual opcional; branco é armazenado como null. //
    public void setInscricaoEstadual(String inscricaoEstadual) {
        this.inscricaoEstadual = normalizarCampoOpcional(inscricaoEstadual);
    }

    // Normaliza o endereço opcional; branco é armazenado como null. //
    public void setEndereco(String endereco) {
        this.endereco = normalizarCampoOpcional(endereco);
    }

    // Atualiza o e-mail sem permitir que ambos os contatos fiquem ausentes. //
    public void setEmail(String email) {
        definirContato(email, this.celular);
    }

    // Atualiza o celular sem permitir que ambos os contatos fiquem ausentes. //
    public void setCelular(String celular) {
        definirContato(this.email, celular);
    }

    // Métodos de acesso //

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public Cnpj getCnpj() {
        return cnpj;
    }

    public String getInscricaoEstadual() {
        return inscricaoEstadual;
    }

    public String getEndereco() {
        return endereco;
    }

    public String getEmail() {
        return email;
    }

    public String getCelular() {
        return celular;
    }

    // Construtor //

    // Cria um cliente, validando os dados obrigatórios e normalizando os opcionais. //
    public Cliente(
        String razaoSocial,
        String cnpj,
        String inscricaoEstadual,
        String endereco,
        String email,
        String celular
    ){
        setRazaoSocial(razaoSocial);
        setCnpj(cnpj);

        this.inscricaoEstadual =
            normalizarCampoOpcional(inscricaoEstadual);

        this.endereco =
            normalizarCampoOpcional(endereco);

        definirContato(email, celular);
    }

    // Validações e normalização //

    private void validarTextoObrigatorio(
        String valor,
        String nomeDoCampo
    ) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(
                nomeDoCampo + " é obrigatório."
            );
        }
    }

    private void validarContato(String email, String celular) {
        boolean emailNaoInformado =
            email == null || email.isBlank();

        boolean celularNaoInformado =
            celular == null || celular.isBlank();

        if (emailNaoInformado && celularNaoInformado) {
            throw new IllegalArgumentException(
                "Informe pelo menos um e-mail ou número de celular."
            );
        }
    }

    private String normalizarCampoOpcional(String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }

        return valor.trim();
    }

    // Mantém a regra de que pelo menos um dos contatos deve ser informado. //
    private void definirContato(String email, String celular) {
        validarContato(email, celular);

        this.email = normalizarCampoOpcional(email);
        this.celular = normalizarCampoOpcional(celular);
    }
}
