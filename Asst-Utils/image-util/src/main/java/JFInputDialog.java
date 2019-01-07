import javax.swing.*;
import java.awt.*;

public class JFInputDialog extends JFrame {


    public JFInputDialog(){

        getContentPane().setBackground(Color.lightGray);
        setTitle("Processing Images In-Frame");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setResizable(false);
        setSize(400, 300);
        setLocationRelativeTo(null);
        getContentPane().setLayout(null);
    }

    public void closeDialog(){
        this.getContentPane().setVisible(false);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.dispose();
    }

}
