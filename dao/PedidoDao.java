package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import conexao.Conexao;
import entidades.Pedido;

public class PedidoDao {
    Connection conexão;

    public PedidoDao(Connection conexão) {
        this.conexão = conexão;
    }

    public void fazerPedido(Pedido p) {
        String sql = "INSERT INTO pedido (NOME,NUMMESA, TELEFONE,PEDIDOF) VALUES (?,?,?,?)";

        PreparedStatement ps = null;
        try {
            ps = Conexao.getconexao().prepareStatement(sql);
            ps.setString(1, p.getNome());
            ps.setString(2, p.getNummesa());
            ps.setString(3, p.getTelefone());
            ps.setString(4, p.getPedidoF());

            ps.execute();
            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void cancelarPedido() {
        String sql = "DELETE FROM pedido\n" +
                "ORDER BY nome,nummesa,telefone,pedidoF\n" +
                "LIMIT 1;\n" + //
                "";
        PreparedStatement ps;
        try {
            ps = this.conexão.prepareStatement(sql);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Pedido> listarPedido() {
        ArrayList<Pedido> pedidos = new ArrayList<>();
        try {
            String query = "SELECT * FROM nome_da_tabela";
            PreparedStatement statement = conexão.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String nome = resultSet.getString("nome");
                String nummesa = resultSet.getString("nummesa");
                String telefone = resultSet.getString("telefone");
                String pedido = resultSet.getString("pedido");

                // Exiba os dados ou faça o que precisar com eles
                Pedido p = new Pedido(nome, nummesa, telefone, pedido);
                pedidos.add(p);
            }

            // Feche os recursos
            resultSet.close();
            statement.close();
            conexão.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pedidos;
    }

    public Pedido buscarPedido(String nummesa) {
        return null;
    }
}
