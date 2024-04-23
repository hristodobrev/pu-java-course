import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class NewFrame extends JFrame {
	JPanel personPanel = new JPanel();
	JPanel carPanel = new JPanel();
	JPanel rentPanel = new JPanel();
	JPanel enquiryPanel = new JPanel();

	JTabbedPane tab = new JTabbedPane();

	public NewFrame() {
		this.setSize(400, 600);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		tab.add(personPanel, "Клиенти");
		tab.add(carPanel, "Коли");
		tab.add(rentPanel, "Наем");
		tab.add(enquiryPanel, "Справки");

		this.add(tab);

		this.setVisible(true);
	}
}
