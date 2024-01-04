package entidades;

public class Pedido {
    private String nome;
    private String nummesa;
    private String telefone;
    private String pedidoF;
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Pedido(String nome2, String mesa, String telefone2, String pedido) {
        this.nome = nome2;
        this.nummesa = mesa;
        this.telefone = telefone2;
        this.pedidoF = pedido;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNummesa() {
        return nummesa;
    }

    public void setNummesa(String nummesa) {
        this.nummesa = nummesa;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getPedidoF() {
        return pedidoF;
    }

    public void setPedidoF(String pedidoF) {
        this.pedidoF = pedidoF;
    }
}
