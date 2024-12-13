public class TransacaoValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransacaoValidator.class);
    private static final List<String> CODIGOS_VALIDOS = List.of("02", "03", "04", "05", "12");

    public void validate(ISOModel isoModel) {
        LOGGER.info("Iniciando validação da transação");

        if (isInvalid(isoModel)) {
            throw new IllegalArgumentException("Valores obrigatórios não preenchidos");
        }

        try {
            processarTransacao(isoModel);
        } catch (IllegalArgumentException e) {
            LOGGER.error("Validação falhou: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            LOGGER.error("Erro inesperado ao validar transação", e);
            throw new RuntimeException("Erro ao processar transação", e);
        }
    }

    private boolean isInvalid(ISOModel isoModel) {
        if (isoModel.getBit02() == null) {
            return true;
        }

        String bit02Value = isoModel.getBit02().getValue();
        boolean isBit02Empty = bit02Value == null || bit02Value.isEmpty();
        boolean isAuxValidation = isBit02Empty && isoModel.getBit03() == null;

        return isBit02Empty && !isAuxValidation;
    }

    private void processarTransacao(ISOModel isoModel) {
        if (!isProcessavel(isoModel)) {
            throw new IllegalArgumentException("Transação inválida para processamento");
        }

        LOGGER.info("Salvando transação com Bit02: {}", isoModel.getBit02().getValue());
        salvarTransacao(isoModel);
    }

    private boolean isProcessavel(ISOModel isoModel) {
        return isoModel.getBit03() != null &&
            isoModel.getBit04() != null &&
            isoModel.getBit05() != null &&
            isoModel.getBit12() != null;
    }

    private void salvarTransacao(ISOModel isoModel) {
        LOGGER.info("Transação salva com sucesso: Bit02 = {}", isoModel.getBit02().getValue());
    }
}
