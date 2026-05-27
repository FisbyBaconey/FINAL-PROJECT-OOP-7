package ThreadHub.model;

public class Buyer extends User {

    public Buyer(int id, String username, String password, String nama) {
        super(id, username, password, nama, "buyer");
    }

    @Override
    public String getDashboardTitle() {
        return "ThreadHub — Selamat Belanja, " + getNama() + "!";
    }
}
