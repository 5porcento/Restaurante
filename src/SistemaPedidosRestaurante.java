package src;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import conexao.Conexao;
import dao.PedidoDao;
import entidades.Pedido;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SistemaPedidosRestaurante extends JFrame {

    public List<String> pedidos = new ArrayList<>();

    public SistemaPedidosRestaurante() {
        super("Sistema de Pedidos para Restaurante");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
        setResizable(false);
        setLocationRelativeTo(null);

        // Componentes da interface

        JLabel labelSelecionarPedido = new JLabel("Selecionar Pedido (N* da mesa):");
        JTextField textFieldSelecionarPedido = new JTextField();

        JButton atualizarStatusButton = new JButton("Atualizar Status");
        atualizarStatusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                // Obtém o número da mesa do pedido a ser atualizado
                try {
                    int numeroMesa = Integer.parseInt(textFieldSelecionarPedido.getText());
                    boolean pedidoEncontrado = false;

                    // Percorre a lista de pedidos para encontrar o pedido com o número da mesa
                    for (int i = 0; i < pedidos.size(); i++) {
                        String pedidoAtual = pedidos.get(i);
                        String[] infoPedido = pedidoAtual.split("\n");
                        String mesaAtual = infoPedido[1].substring(6);

                        if (Integer.parseInt(mesaAtual) == numeroMesa) {
                            pedidoEncontrado = true;

                            // Extraindo informações do pedido para exibir em um JOptionPane
                            String nome = infoPedido[0].substring(6);
                            String telefone = infoPedido[2].substring(10);
                            String pedidoTexto = infoPedido[3].substring(8);
                            String statusAtual = infoPedido[4].substring(18);

                            // Exibe um JOptionPane para selecionar o novo status
                            String novoStatus = (String) JOptionPane.showInputDialog(
                                    SistemaPedidosRestaurante.this,
                                    "Selecione o novo status:",
                                    "Atualizar Status do Pedido",
                                    JOptionPane.QUESTION_MESSAGE,
                                    null,
                                    new String[] { "Em andamento", "Pronto", "Entregue" },
                                    statusAtual);

                            // Atualiza o status do pedido
                            if (novoStatus != null) {
                                pedidos.set(i, "Nome: " + nome + "\nMesa: " + mesaAtual + "\nTelefone: " + telefone
                                        + "\nPedido: " + pedidoTexto + "\nStatus do Pedido: " + novoStatus);
                                JOptionPane.showMessageDialog(SistemaPedidosRestaurante.this,
                                        "Status do pedido atualizado com sucesso!");
                            }

                            // Sai do loop, pois já encontrou o pedido
                            break;
                        }
                    }

                    if (!pedidoEncontrado) {
                        JOptionPane.showMessageDialog(SistemaPedidosRestaurante.this,
                                "Número da mesa não encontrado!",
                                "Erro",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(SistemaPedidosRestaurante.this,
                            "Digite um número válido para o número da mesa.",
                            "Erro",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(9, 6));

        JLabel labelNome = new JLabel("Nome:");
        JTextField textFieldNome = new JTextField();

        JLabel labelMesa = new JLabel("Número da Mesa:");
        JTextField textFieldMesa = new JTextField();

        JLabel labelTelefone = new JLabel("Telefone:");
        JTextField textFieldTelefone = new JTextField();

        JLabel labelPedido = new JLabel("Pedido:");
        JTextField textFieldPedido = new JTextField();

        JLabel labelStatus = new JLabel("Status do Pedido:");
        JComboBox<String> statusComboBox = new JComboBox<>(new String[] { "Em andamento", "Pronto", "Entregue" });

        // Botões

        JButton fazerPedidoButton = new JButton("Fazer Pedido");
        fazerPedidoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent event) {
                String nome = textFieldNome.getText();
                String mesa = textFieldMesa.getText();
                String telefone = textFieldTelefone.getText();
                String pedido = textFieldPedido.getText();
                String status = (String) statusComboBox.getSelectedItem();
                Pedido p = new Pedido(nome, mesa, telefone, pedido);
                // add no banco
                Conexao o = new Conexao();
                PedidoDao e = new PedidoDao(Conexao.getconexao());
                e.fazerPedido(p);
                // clear na label
                textFieldNome.setText("");
                textFieldMesa.setText("");
                textFieldPedido.setText("");
                textFieldTelefone.setText("");

                if (!nome.isEmpty() && !mesa.isEmpty() && !telefone.isEmpty() && !pedido.isEmpty()) {
                    adicionarPedido(nome, mesa, telefone, pedido, status);
                } else {
                    JOptionPane.showMessageDialog(SistemaPedidosRestaurante.this,
                            "Preencha todos os campos para fazer um pedido.",
                            "Erro",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JButton cancelarButton = new JButton("Cancelar");
        cancelarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent event) {
                Conexao o = new Conexao();
                PedidoDao e = new PedidoDao(Conexao.getconexao());
                e.cancelarPedido();
                cancelarPedido();
            }
        });

        // Adiciona componentes ao painel
        panel.add(labelNome);
        panel.add(textFieldNome);
        panel.add(labelMesa);
        panel.add(textFieldMesa);
        panel.add(labelTelefone);
        panel.add(textFieldTelefone);
        panel.add(labelPedido);
        panel.add(textFieldPedido);
        panel.add(labelStatus);
        panel.add(statusComboBox);
        panel.add(fazerPedidoButton);
        panel.add(cancelarButton);

        // Adiciona painel à interface
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(panel, BorderLayout.CENTER);
        panel.add(labelSelecionarPedido);
        panel.add(textFieldSelecionarPedido);
        panel.add(atualizarStatusButton);

        // Menu para ver pedidos
        JMenuBar menuBar = new JMenuBar();
        JMenu menuVerPedidos = new JMenu("Ver Pedidos");
        JMenuItem itemVerPedidos = new JMenuItem("Visualizar Todos os Pedidos");
        itemVerPedidos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exibirPedidos();
            }
        });
        menuVerPedidos.add(itemVerPedidos);

        // Adiciona item de menu para gerar relatório PDF
        JMenuItem itemGerarRelatorio = new JMenuItem("Gerar Relatório PDF");
        itemGerarRelatorio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    escolherLocalSalvarRelatorio();
                } catch (FileNotFoundException ex) {
                    JOptionPane.showMessageDialog(SistemaPedidosRestaurante.this,
                            "Erro ao gerar o relatório PDF.",
                            "Erro",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        menuVerPedidos.add(itemGerarRelatorio);

        menuBar.add(menuVerPedidos);
        setJMenuBar(menuBar);
    }

    private void adicionarPedido(String nome, String mesa, String telefone, String pedido, String status) {
        pedidos.add(
                "Nome: " + nome + "\nMesa: " + mesa + "\nTelefone: " + telefone + "\nPedido: " + pedido
                        + "\nstatus do pedido:"
                        + status);
        JOptionPane.showMessageDialog(this,
                "Pedido feito com sucesso!");
        limparCampos();
    }

    private void cancelarPedido() {
        if (!pedidos.isEmpty()) {
            // Remove o pedido mais recente
            pedidos.remove(pedidos.size() - 1);
            JOptionPane.showMessageDialog(this, "Pedido cancelado com sucesso!");
        } else {
            JOptionPane.showMessageDialog(this, "Não há pedidos para cancelar.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
        limparCampos();
    }

    private void exibirPedidos() {
        if (pedidos.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhum pedido foi feito ainda.");
        } else {
            StringBuilder mensagem = new StringBuilder("Pedidos:\n");
            for (String pedido : pedidos) {
                mensagem.append("- ").append(pedido).append("\n\n");
            }
            JOptionPane.showMessageDialog(this, mensagem.toString());
        }
    }

    private void limparCampos() {
        getContentPane().getComponents();
        Component[] components = getContentPane().getComponents();
        for (Component component : components) {
            if (component instanceof JTextField) {
                ((JTextField) component).setText("");
            }
        }
    }

    private void escolherLocalSalvarRelatorio() throws FileNotFoundException {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Salvar Relatório PDF");
        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            gerarRelatorioPDF(fileToSave.getAbsolutePath());
        }
    }

    private void gerarRelatorioPDF(String dest) throws FileNotFoundException {
        PdfWriter writer = new PdfWriter(dest);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        try {
            // Adicionando fonte personalizada
            PdfFont font = PdfFontFactory.createFont("Helvetica-Bold");

            // Adicionando cabeçalho
            Paragraph header = new Paragraph("Relatório de Pedidos")
                    .setFont(font)
                    .setFontSize(20)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(20);
            document.add(header);

            // Adicionando linha de separação
            document.add(new Paragraph(" ").setBorder(new SolidBorder(ColorConstants.BLACK, 1)));

            // Adicionando tabela
            float[] columnWidths = {1, 5}; // Largura das colunas
            Table table = new Table(UnitValue.createPercentArray(columnWidths));
            table.setWidth(UnitValue.createPercentValue(100));

            // Adicionando cabeçalho da tabela
            table.addHeaderCell(new Cell().add(new Paragraph("ID").setFont(font).setFontSize(12)).setBackgroundColor(ColorConstants.LIGHT_GRAY));
            table.addHeaderCell(new Cell().add(new Paragraph("Descrição do Pedido").setFont(font).setFontSize(12)).setBackgroundColor(ColorConstants.LIGHT_GRAY));

            // Adicionando dados dos pedidos
            int id = 1;
            for (String pedido : pedidos) {
                table.addCell(new Cell().add(new Paragraph(String.valueOf(id++))));
                table.addCell(new Cell().add(new Paragraph(pedido)));
            }

            document.add(table);

            // Adicionando rodapé
            Paragraph footer = new Paragraph("Relatório gerado automaticamente.")
                    .setFontSize(10)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginTop(20);
            document.add(footer);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            document.close();
        }

        JOptionPane.showMessageDialog(this, "Relatório PDF gerado com sucesso!");
    }

}
