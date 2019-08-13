package com.celio.fac.dao;

import android.provider.BaseColumns;

public class Entities {

    /**
     * Tabela de clientes
     */
    public static class ClienteEntry implements BaseColumns {

        public static final String TABLE_NAME = "clientes";
        public static final String COLUMN_NOME_CLIENTE = "nome_cliente";
        public static final String COLUMN_SEGMENTO = "segmento";
        public static final String COLUMN_LOGRADOURO = "logradouro";
        public static final String COLUMN_NUMERO = "numero";
        public static final String COLUMN_BAIRRO = "bairro";
        public static final String COLUMN_CIDADE = "cidade";
        public static final String COLUMN_TELEFONE = "telefone";
        public static final String COLUMN_CELULAR = "celular";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_NOME_CONTATO = "nome_contato";
        public static final String COLUMN_USA_OUTRA_FARINHA = "usa_outra_farinha";
        public static final String COLUMN_MARCA_FARINHA = "marca_farinha";
        public static final String COLUMN_ID_VENDEDOR = "idvendedor";
    }

    /**
     * tabela vendedores
     */
    public static class VendedorEntry implements BaseColumns {

        public static final String TABLE_NAME = "vendedores";
        public static final String COLUMN_NOME_VENDEDOR = "nome_vendedor";
        public static final String COLUMN_ID_SUPERVISOR = "idsupervidor";
    }

    /**
     * tabela supervisores
     */
    public static class SupervisorEntry implements BaseColumns {

        public static final String TABLE_NAME = "supervisores";
        public static final String COLUMN_NOME_SUPERVISOR = "nome_supervisor";
    }

    /**
     * tabela tecnicos
     */
    public static class TecnicoEntry implements BaseColumns {

        public static final String TABLE_NAME = "tecnicos";
        public static final String COLUMN_NOME_TECNICO = "nome_tecnico";
    }


    /**
     * tabela de produtos
     */
    public static class ProdutoEntry implements BaseColumns {
        public static final String TABLE_NAME = "produtos";
        public static final String COLUMN_NOME_PRODUTO = "nome_produto";
    }

    /**
     * tabela de massas
     */
    public static class MassaEntry implements BaseColumns {
        public static final String TABLE_NAME = "massas";
        public static final String COLUMN_NOME_MASSA = "nome_massa";
        public static final String COLUMN_QTD_PAES_BOLA = "qtd_paes_bola";
        public static final String COLUMN_VELOCIDADE_A = "velocidade_a";
        public static final String COLUMN_VELOCIDADE_B = "velocidade_b";
        public static final String COLUMN_TEMP_AGUA = "temperatura_agua";
        public static final String COLUMN_TEMP_MASSA = "temperatura_massa";
        public static final String COLUMN_TEMP_FORNO = "temperatura_forno";
        public static final String COLUMN_FERMENTACAO = "fermentacao";
        public static final String COLUMN_TEMPO_FERMENTACAO = "tempo_fermentacao";
        public static final String COLUMN_TEMPO_FORNEAMENTO = "tempo_forneamento";
        public static final String COLUMN_PESO_BOLA = "peso_bola";
        public static final String COLUMN_PESO_PAO_CRU = "peso_pao_cru";
        public static final String COLUMN_PESO_PAO_ASSADO = "peso_pao_assado";
    }

    /**
     * tabela de ingredientes
     */
    public static class IngredienteEntry implements BaseColumns {
        public static final String TABLE_NAME = "ingredientes";
        public static final String COLUMN_NOME_INGREDIENTE = "nome_ingrediente";
    }

    /**
     * tabela de receitas
     */
    public static class ReceitaEntry implements BaseColumns {
        public static final String TABLE_NAME = "receitas";
        public static final String COLUMN_ID_MASSA = "idmassa";
        public static final String COLUMN_ID_INGREDIENTE = "idingrediente";
        public static final String COLUMN_PORCENTAGEM = "porcentagem";
    }
}
