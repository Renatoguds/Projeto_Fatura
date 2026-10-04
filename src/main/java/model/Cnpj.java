package model;
// Representa um CNPJ validado e armazenado sem pontuação. //
public final class Cnpj {

    // Atributos //

    private static final int TAMANHO = 14;
    private final String numero;

    // Pesos usados no cálculo dos dois dígitos verificadores do CNPJ. //
    private static final int[] PESOS_PRIMEIRO_DIGITO = {
        5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2
    };

    private static final int[] PESOS_SEGUNDO_DIGITO = {
        6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2
    };

    // Métodos de acesso //

    // Retorna os 14 dígitos do CNPJ, sem pontuação. //
    public String getNumero() {
        return numero;
    }

    // Construtor //

    // Cria um CNPJ normalizado e valida formato e dígitos verificadores. //
    public Cnpj(String valor) {
        validarPreenchimento(valor);
        validarFormato(valor);

        String numeroSemFormatacao = removerFormatacao(valor);

        validar(numeroSemFormatacao);

        this.numero = numeroSemFormatacao;
    }

    // Validações //

    private void validarPreenchimento(String valor) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(
                "O CNPJ é obrigatório."
            );
        }
    }

    private void validarFormato(String valor) {
        boolean formatoNumerico = valor.matches("\\d{14}");

        boolean formatoPontuado = valor.matches(
            "\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}"
        );

        if (!formatoNumerico && !formatoPontuado) {
            throw new IllegalArgumentException(
                "O formato do CNPJ é inválido."
            );
        }
    }

    private String removerFormatacao(String valor) {
        return valor.replaceAll("[./-]", "");
    }

    private void validar(String numero) {
        validarTamanho(numero);
        validarSequenciaRepetida(numero);
        validarDigitosVerificadores(numero);
    }

    private void validarTamanho(String numero) {
        if (numero.length() != TAMANHO) {
            throw new IllegalArgumentException(
                "O CNPJ deve possuir 14 dígitos."
            );
        }
    }

    private void validarSequenciaRepetida(String numero) {
        if (numero.matches("(\\d)\\1{13}")) {
            throw new IllegalArgumentException(
                "O CNPJ não pode conter somente números repetidos."
            );
        }
    }

    private void validarDigitosVerificadores(String numero) {
        String base = numero.substring(0, 12);

        int primeiroInformado =
            Character.getNumericValue(numero.charAt(12));

        int segundoInformado =
            Character.getNumericValue(numero.charAt(13));

        int primeiroCalculado = calcularDigito(
            base,
            PESOS_PRIMEIRO_DIGITO
        );

        String baseComPrimeiroDigito =
            base + primeiroCalculado;

        int segundoCalculado = calcularDigito(
            baseComPrimeiroDigito,
            PESOS_SEGUNDO_DIGITO
        );

        if (
            primeiroInformado != primeiroCalculado
            || segundoInformado != segundoCalculado
        ) {
            throw new IllegalArgumentException(
                "Os dígitos verificadores do CNPJ são inválidos."
            );
        }
    }

    private int calcularDigito(String base, int[] pesos) {
        int soma = 0;

        for (int i = 0; i < base.length(); i++) {
            int digito = Character.getNumericValue(base.charAt(i));
            soma += digito * pesos[i];
        }

        int resto = soma % 11;

        if (resto < 2) {
            return 0;
        }

        return 11 - resto;
    }

    // Representação e igualdade //

    // Retorna o CNPJ no formato 00.000.000/0000-00. //
    public String formatado() {
        return String.format(
            "%s.%s.%s/%s-%s",
            numero.substring(0, 2),
            numero.substring(2, 5),
            numero.substring(5, 8),
            numero.substring(8, 12),
            numero.substring(12, 14)
        );
    }

    @Override
    public String toString() {
        return formatado();
    }

    @Override
    public boolean equals(Object objeto) {
        if (this == objeto) {
            return true;
        }

        if (objeto == null || getClass() != objeto.getClass()) {
            return false;
        }

        Cnpj outroCnpj = (Cnpj) objeto;

        return numero.equals(outroCnpj.numero);
    }

    @Override
    public int hashCode() {
        return numero.hashCode();
    }
}
