package model;

public class Cliente {
    //--------------------Atributos--------------------//
    
    private String razaoSocial;
    private Cnpj cnpj;
    private String inscricaoEstadual;
    private String endereco;
    private String email;
    private String celular;
    
    //--------------------Construtor-------------------//

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
        validarContato(email, celular);
        
        this.inscricaoEstadual = 
            normalizarCampoOpcional(inscricaoEstadual);

        this.endereco = 
            normalizarCampoOpcional(endereco);

        definirContato(email, celular);
    }

    //---------------------Métodos---------------------//

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
    
    // ---------------Setters--------------- //

    private void definirContato(String email, String celular) {
        validarContato(email, celular);

        this.email = normalizarCampoOpcional(email);
        this.celular = normalizarCampoOpcional(celular);
    }

    public void setRazaoSocial(String razaoSocial) {
        validarTextoObrigatorio(
            razaoSocial,
            "A razão social"
        );

        this.razaoSocial = razaoSocial.trim();
    }
    
    public void setCnpj(String cnpj) {
        this.cnpj = new Cnpj(cnpj);
    }

    public void setInscricaoEstadual(String inscricaoEstadual) {
        normalizarCampoOpcional(inscricaoEstadual);
        this.inscricaoEstadual = inscricaoEstadual;
    }

    public void setEndereco(String endereco) {
        normalizarCampoOpcional(endereco);
        this.endereco = endereco;
    }

    public void setEmail(String email) {
        definirContato(email, this.celular);
    }

    public void setCelular(String celular) {
        definirContato(this.email, celular);
    }

    // ---------------Getters--------------- // 

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
    
}
