import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class ShapeCards implements ItemListener {
    JPanel cards; //a panel that uses CardLayout
    CardLayout cl;
    final static String circle = "Circle";
    final static String square = "Square";
    final static String rectangle = "Rectangle";
    String[] shapes = {circle, square, rectangle};
    JComboBox shape = new JComboBox(shapes);
    JButton clear = new JButton("CLEAR FIELDS");
    float surfaceArea;
    float area;
    float pi = (float) Math.PI;

    public void addComponentToPane(Container pane) {

        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));

        JPanel readmePanel = new JPanel();
        JLabel instructions = new JLabel();
        instructions.setText("<HTML><br>Please select a shape from the drop down menu<br>followed by entering the measurement into the text box<br></HTML>");
        instructions.setHorizontalTextPosition(JLabel.CENTER);
        instructions.setVerticalTextPosition(JLabel.CENTER);
        readmePanel.add(instructions);

        //Put the JComboBox in a JPanel to get a nicer look.
        JPanel comboBoxPanel = new JPanel(); //use FlowLayout
        shape.setEditable(false);
        shape.addItemListener(this);
        comboBoxPanel.add(shape);

        //Set up Document Filter to only accept integers
        DocumentFilter myDocFilter = new DocumentFilter(){
            Pattern regEx = Pattern.compile("\\d*");

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                Matcher matcher = regEx.matcher(text);
                if(!matcher.matches()){
                    return;
                }
                super.replace(fb, offset, length, text, attrs);
            }
        };


        //Create the panel which will hold the circumference text box and place it in another panel in order to give it a BoxLayout
        JPanel circleTextBox = new JPanel();
        JPanel circPanel = new JPanel();
        circPanel.setLayout(new BoxLayout(circPanel, BoxLayout.Y_AXIS));
        JFormattedTextField circText = new JFormattedTextField();
        ((AbstractDocument)circText.getDocument()).setDocumentFilter(myDocFilter);
        circText.setColumns(10);
        JLabel circTitle = new JLabel("Circumference:");
        circPanel.add(circTitle);
        circPanel.add(circText);
        circleTextBox.add(circPanel);

        //Do the same with the panels here NOTE: As above, DocFilter has been set to apply to the JFormattedTextField
        JPanel squareTextBox = new JPanel();
        JPanel lengthPanel = new JPanel();
        lengthPanel.setLayout(new BoxLayout(lengthPanel, BoxLayout.Y_AXIS));
        JFormattedTextField lengthText = new JFormattedTextField();
        ((AbstractDocument)circText.getDocument()).setDocumentFilter(myDocFilter);
        lengthText.setColumns(10);
        JLabel lengthTitle = new  JLabel("Length:");
        lengthPanel.add(lengthTitle);
        lengthPanel.add(lengthText);
        squareTextBox.add(lengthPanel);

        //As above except an extra panel created as there are two JFormattedTextFields rather than one
        JPanel rectangleTextBox = new JPanel();
        JPanel lengthPanel1 = new JPanel();
        lengthPanel1.setLayout(new BoxLayout(lengthPanel1, BoxLayout.Y_AXIS));
        JPanel widthPanel = new JPanel();
        widthPanel.setLayout(new BoxLayout(widthPanel, BoxLayout.Y_AXIS));
        JFormattedTextField lengthText1 = new JFormattedTextField();
        ((AbstractDocument)lengthText1.getDocument()).setDocumentFilter(myDocFilter);
        JFormattedTextField widthText = new JFormattedTextField();
        ((AbstractDocument)widthText.getDocument()).setDocumentFilter(myDocFilter);
        lengthText1.setColumns(10);
        widthText.setColumns(10);
        JLabel lengthTitle1 = new  JLabel("Length:");
        JLabel widthTitle = new JLabel("Width:");
        lengthPanel1.add(lengthTitle1);
        lengthPanel1.add(lengthText1);
        widthPanel.add(widthTitle);
        widthPanel.add(widthText);
        rectangleTextBox.add(lengthPanel1);
        rectangleTextBox.add(widthPanel);


        //Create the cards and the panel that contains them
        CardLayout cl = new CardLayout();
        cards = new JPanel();
        cards.setLayout(cl);
        cards.add(circleTextBox, circle);
        cards.add(squareTextBox, square);
        cards.add(rectangleTextBox, rectangle);

        //Create the result label that will be updated on the press of the JButton (below) to show the result of the JFormattedTextField input's processing
        JPanel resultPanel = new JPanel();
        JLabel result = new JLabel();
        result.setText("<HTML><br>Place some values and run to get results<br><br><HTML>");

        //Create the panel to hold the panel and upload and create the icon to go into the button
        JPanel buttonPanel = new JPanel();
        ImageIcon goImage = new ImageIcon("images/gocalc.png");
        Image image = goImage.getImage();
        Image newimg = image.getScaledInstance(20, 20,  java.awt.Image.SCALE_SMOOTH);
        goImage = new ImageIcon(newimg);

        //Adds icon image to Jbutton, sets it's alignment and adds actionlistener
        JButton go = new JButton("Press to run", goImage);
        go.setHorizontalTextPosition(AbstractButton.LEFT);
        go.setVerticalTextPosition(AbstractButton.CENTER);
        go.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent evt) {

                    //This makes s equal the content of the JComboBox (in form of 'shapes' array)
                    String s = (String) shape.getSelectedItem();

                    if (s == circle) {

                        //JFormattedTextField's content is converted to Float in order to calculate surface area/area
                        float circ = Float.parseFloat(circText.getText());
                        surfaceArea = (2.0f * circ) * pi;
                        area = (circ * circ) * pi;
                        result.setText("<HTML><br>Circumference:   " + surfaceArea + "<br>Area:   " + area + "</HTML>)");

                    } else if (s == square) {

                        float length = Float.parseFloat(lengthText.getText());
                        surfaceArea = (4 * length);
                        area = (length * length);
                        result.setText("<HTML><br>Surface Area:   " + surfaceArea + "<br>Area:   " + area + "</HTML>");

                    } else if (s == rectangle) {

                        //Two floats created here to represent the data from the two text fields that appear when 'rectangle' is chosen from JComboBox
                        float length1 = Float.parseFloat(lengthText1.getText());
                        float width = Float.parseFloat(widthText.getText());
                        surfaceArea = (2 * length1) + (2 * width);
                        area = (length1 * width);
                        result.setText("<HTML><br>Surface Area:   " + surfaceArea + "<br>Area:   " + area + "</HTML>");

        } else {
            surfaceArea = 0.0f;
            area = 0.0f;
            result.setText("<HTML><br>Place some values and run to get results<HTML>");
                    }
                };
        });

        //Add go button to its panel
        buttonPanel.add(go);

        //Adjusting positioning of results label and adding it to the panel
        result.setHorizontalTextPosition(JLabel.CENTER);
        result.setVerticalTextPosition(JLabel.CENTER);
        resultPanel.add(result);

        //Add all the panels to the main component pane
        pane.add(readmePanel, BorderLayout.PAGE_START);
        pane.add(comboBoxPanel, BorderLayout.CENTER);
        pane.add(cards, BorderLayout.CENTER);
        pane.add(buttonPanel, BorderLayout.CENTER);
        pane.add(resultPanel, BorderLayout.CENTER);
    }

        @Override
        public void itemStateChanged(ItemEvent e) {

        //This item event will bring up the relevant card in accordance to the shape selected in the JComboBox
            CardLayout cl = (CardLayout) (cards.getLayout());
            cl.show(cards, (String) e.getItem());
        }


    private static void createAndShowGUI() {

        //Create and set up the window
        JFrame frame = new JFrame("Shape Calculator V1");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane
        ShapeCards demo = new ShapeCards();
        demo.addComponentToPane(frame.getContentPane());

        //Display the window
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {

        try {

            //Add UI interface
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }

        UIManager.put("swing.boldMetal", Boolean.FALSE);

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}