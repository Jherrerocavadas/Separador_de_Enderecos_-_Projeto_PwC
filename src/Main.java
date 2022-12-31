/*----------------------------------------------------------------------------------------------------------*\
* Programa: Separador de endereços em nome e número da rua                                                  *
* Autor: Johann Herrero Cavadas                                                                             *
* Plataforma: Java JDK 18.0.2                                                                               *
\*----------------------------------------------------------------------------------------------------------*/

/*================================== Importação de bibliotecas e classes. ==================================*/
import javax.swing.*;
import java.util.Arrays;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
/*------------------------------------ Declaração de variáveis (globais). ------------------------------------*/

        // Opções do menu de opções
        String [] options = {"Rodar casos de teste",
                "Inserir caso de teste", "Sair"};
        boolean isRunning = true; // Variável para manter o programa rodando em loop até que seja fechado pelo usuário.

        while(isRunning){
            int opcao_escolhida = JOptionPane.showOptionDialog(null,
                    "Olá! escolha uma opção para prosseguir",
                    "Separador de endereços",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null, options, options[0]);

            // switch funcionando como um "menu" do programa.
            switch (opcao_escolhida){
                case 0:
                    casosTeste();
                    break;

                case 1:
                    inserirCaso();
                    break;

                case 2:
                    isRunning = false;
                    break;

                default:
                    isRunning = false;
                    break;
            }
        }
    }


/*============================ Criação de função para processamento da entrada. =============================*/

    /*A função abaixo realiza a separação do endereço de entrada fornecido, aplicando alguns procedimentos
    * simples de normalização dos dados. A função consegue realizar a separação desde endereços mais simples
    * (Um nome e numeral da rua), passando pelos mais complexos (Múltiplos nomes e numeral da rua com complemento)
    * até alguns padrões de endereços internacionais (Nomes de ruas envolvendo numerais, número da rua inserido
    * antes do nome e mesmo números da rua com algum prefixo). Requer como argumento uma string contendo o ende-
    * reço completo (ou seja, concatenado) com o nome e número da rua, retornando um Array de string com o nome
    * e o número da rua separados.*/
    public static String[] separadorEndereco(String endereco_entrada){

/*---------------------------------------- Declaração de variáveis. ----------------------------------------*/
        String endereco_filtrado; //String que armazenará o endereço sem "normalizado", isto é, sem vírgulas.
        String[] endereco_processo; //Array de strings que armazenará as palavras e números divididos.
        String endereco_nome = new String(); //String que armazenará a parte do nome da rua.
        String endereco_numero = new String(); //String que armazenará a parte do número da rua.
        String[] endereco_saida = new String[2]; //Array de strings que armazenará os resultados de saída.


/*--------------------- Processar o endereço inserido (divisão das palavras e números). --------------------*/

        //Remover as vírgulas dos endereços que a incluem.
        endereco_filtrado = endereco_entrada.replaceAll(",","");

        //Separar as palavras quando detectar o caractere de espaço.
        endereco_processo = endereco_filtrado.split(" ");


/*-------------------------------- Organização do endereço em rua e número. --------------------------------*/

        for (int i = 0; i < endereco_processo.length; i++){

            //regex que procura os números
            Pattern regex_num = Pattern.compile("[0-9]+");

            //regex que procura uma letra do alfabeto, desconsiderando maiúsculas e minúsculas.
            Pattern regex_letters = Pattern.compile("[A-z]");

            // Se o i estiver nas casas possíveis de números (primeira e última casa):
            if((i == 0 || i == endereco_processo.length -1) && regex_num.matcher(endereco_processo[i]).find())
            {
                endereco_numero += endereco_processo[i]; //adiciona na string do número da rua
            }

            // Se o i estiver na penúltima casa (onde pode ter um número que será seguido de letra)
            else if (i == endereco_processo.length -2 && regex_num.matcher(endereco_processo[i]).find() && regex_letters.matcher(endereco_processo[i+1]).find())
            {
                endereco_numero += endereco_processo[i]; //adiciona na string do número da rua
                i++; // Vai forçar o for a pular a verificação do próximo elemento, que será uma letra adicionada ao número (caso especial)
                endereco_numero += " " + endereco_processo[i]; //adiciona a letra na string do número da rua (com espaçamento)
            }

            // Se o i estiver na penúltima casa e o texto for "No" (caso especial de endereço internacional)
            else if(i == endereco_processo.length -2 && endereco_processo[i].equalsIgnoreCase("No"))
            {
                endereco_numero += endereco_processo[i] + " "; //adiciona o prefixo "No" (com espacamento e ignorando letras maiúsculas e minúsculas) na string do número da rua
            }

            // Se não se enquadrar nas regras do número da rua (quando for parte do nome da rua)
            else {
                endereco_nome += (endereco_processo[i] + " "); //Adicionar as Strings do Array na parte de nomes do endereço.
            }
        }

/*----------- Repassar os valores das variáveis provisórias para a string de saída e retorná-la. -----------*/

        endereco_saida[0] = endereco_nome;
        endereco_saida[1] = endereco_numero;

        return endereco_saida;
    }


/*==================== Criação de função para execução automatizada dos casos de teste. ====================*/

    /* A função abaixo realiza a execução automatizada dos casos de teste salvos na variável local "enderecos_teste.
    *  Assim, ela realiza a separação de cada endereço fornecido, retornando o resultado no terminal e em uma janela
    *  do JOptionPane. Não requer nenhum argumento e não retorna nada.*/
    public static void casosTeste(){

/*---------------------------------------- Declaração de variáveis. ----------------------------------------*/
        String [] enderecos_teste = new String[9]; //

        enderecos_teste[0] = "Miritiba 339";
        enderecos_teste[1] = "Babaçu 500";
        enderecos_teste[2] = "Cambuí 804B";
        enderecos_teste[3] = "Rio Branco 23";
        enderecos_teste[4] = "Quirino dos Santos 23 b";
        enderecos_teste[5] = "4, Rue de la République";
        enderecos_teste[6] = "100 Broadway Av";
        enderecos_teste[7] = "Calle Sagasta, 26";
        enderecos_teste[8] = "Calle 44 No 1991";

/*----------------------------- Separação dos endereços de cada caso de teste. ------------------------------*/

        for (int i = 0; i < enderecos_teste.length; i++) {
            // Exibição do resultado no terminal
            System.out.println(Arrays.toString(separadorEndereco(enderecos_teste[i])));

            // Exibição do resultado em uma janela do JOptionPane
            JOptionPane.showMessageDialog(null, Arrays.toString(separadorEndereco(enderecos_teste[i])), "Endereço separado", JOptionPane.INFORMATION_MESSAGE);
        }
    }

/*================== Criação de função geradora da janela de inserção de casos manualmente. ==================*/

/* A função abaixo atua como uma geradora da janela para inserção dos casos de teste manualmente. Não requer
* nenhum argumento e não retorna nada */
    public static void inserirCaso(){
/*---------------------------------------- Declaração de variáveis. ----------------------------------------*/
        String endereco_entrada; // String que armazenará o endereço concatenado.
        String[] endereco_saida; // Array de strings que armazenará os resultados de saída.

/*------------------------------------- Receber a entrada do endereço. --------------------------------------*/

        endereco_entrada = JOptionPane.showInputDialog(null, "Inserir endereço concatenado", "Inserir endereço", JOptionPane.INFORMATION_MESSAGE);
        endereco_saida = separadorEndereco(endereco_entrada);

/*------------------------------ Processamento da entrada e retorno dos dados. -------------------------------*/

        // Exibição do resultado no terminal
        System.out.println(Arrays.toString(endereco_saida));

        // Exibição do resultado em uma janela do JOptionPane
        JOptionPane.showMessageDialog(null, Arrays.toString(endereco_saida), "Endereço separado", JOptionPane.INFORMATION_MESSAGE);

    }
}