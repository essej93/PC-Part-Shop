import java.util.ArrayList;
import java.util.Random;
import java.util.Formatter;

import java.io.*;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

import java.util.NoSuchElementException;
import java.util.FormatterClosedException;
import java.util.InputMismatchException;


class CSFrame extends JFrame {

    // variables used to access by the constructor/handlers
    private GridLayout gridLayout;
    private Container container;
    private JButton[] buttons;
    private JLabel[] labels;
    private JList partJList;
    private JTextArea partInfoTextArea;
    private JTextArea customerInfoTextArea;
    private JList orderJList;
    private DefaultListModel<String> orderListModel;
    private JTextArea orderInfoTextArea;
    private JList pastOrderJList;
    private DefaultListModel<String> pastOrderJListModel;
    private JTextArea pastOrderInfoTextArea;
    private JFrame newCustomerFrame;
    private JButton[] newCustomerConfirmationButtons;
    private JComboBox<String> newCustomerGenderComboBox;
    private JTextField[] newCustomerTextFields;
    private OrderingSystem orderingSystem;
    private OrderingSystemHandler handler;
    private static final String[] buttonNames = {"Login as existing customer", "Login as New Customer", "Add item to order", 
    "Remove item from Current Order", "Submit Current Order", "Delete Order", "Export Orders", "Exit the System"};
    private static final String[] labelNames = {"Computer Part List", "Computer Part Information", "Current Order",
    "Current Order Information", "Historical Orders", "Historical Order Information"};
    
    // main method used to instantiate CSFrame object, sets frame size and makes visible
    public static void main(String[] args){

        CSFrame javaFrame = new CSFrame("Computer Shop Ordering System");
        javaFrame.setSize(500, 1000);
        javaFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        javaFrame.setVisible(true);


    }

    // Main constructor for CSFrame
    public CSFrame(String frameName) {
        super(frameName);
        
        // creates OrderingSystem using compshop.ser, if exception is thrown due to no file found or invalid version 
        // the default shop initialisation will run and then generate a new .ser file within the CreateOrderingSystem class
        // the .ser file will then be used to load for all future executions of the program
        // this setup assists when changes are made to the OrderingSystem class and will auto generate a new .ser file when required
        try{
            orderingSystem = new CreateOrderingSystem("compshop.ser").getOrderingSystem();
        }
        catch (IOException invalidFile){
            JOptionPane.showMessageDialog(null, invalidFile, "File Invalid", JOptionPane.WARNING_MESSAGE);
            orderingSystem = new OrderingSystem();
            orderingSystem.initialiseShop();
            CreateOrderingSystem orderingSystemCreation = new CreateOrderingSystem(orderingSystem, "compshop.ser");
        }
        catch(ClassNotFoundException noFile){
            JOptionPane.showMessageDialog(null, noFile, "No File Found", JOptionPane.WARNING_MESSAGE);
            orderingSystem = new OrderingSystem();
            orderingSystem.initialiseShop();
            CreateOrderingSystem orderingSystemCreation = new CreateOrderingSystem(orderingSystem, "compshop.ser");
        }
        
        // creates layout
        gridLayout = new GridLayout(11, 2, 5, 5);

        // initialises handler object
        handler = new OrderingSystemHandler();

        //initialises JButton/Label arrays
        buttons = new JButton[buttonNames.length];
        labels = new JLabel[labelNames.length];
        container = getContentPane();

        // sets layout
        setLayout(gridLayout);

        // creates labels based on label names in labelNames array
        for(int i = 0; i < labelNames.length; i++){
            labels[i] = new JLabel(labelNames[i]);
        }

        // creates buttons based on button names in buttonNames array
        // adds listener to each button
        for(int i = 0; i < buttonNames.length; i++){
            buttons[i] = new JButton(buttonNames[i]);
            buttons[i].addActionListener(handler);
        }

        // creates partJList and gets the part list from the orderingSystem. adds listener to partJList and sets default selection to single.
        partJList = new JList<String>(orderingSystem.getPartList());
        partJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        partJList.addListSelectionListener(handler);

        // creates orderJList and list model for editing. adds listener to order JList and sets default selection to single.
        orderListModel = new DefaultListModel<String>();
        orderJList = new JList<String>(orderListModel);
        orderJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        orderJList.addListSelectionListener(handler);

         // creates pastOrderJList and list model for editing. adds listener to order JList and sets default selection to single.
        pastOrderJListModel = new DefaultListModel<String>();
        pastOrderJList = new JList<String>(pastOrderJListModel);
        pastOrderJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        pastOrderJList.addListSelectionListener(handler);

        // creates text area's for main frame
        partInfoTextArea = new JTextArea(10,15);
        customerInfoTextArea = new JTextArea(10,15);
        orderInfoTextArea = new JTextArea(10,15);
        pastOrderInfoTextArea = new JTextArea(10,15);

        // makes text area's un editable
        partInfoTextArea.setEditable(false);
        customerInfoTextArea.setEditable(false);
        orderInfoTextArea.setEditable(false);
        pastOrderInfoTextArea.setEditable(false);


        // adds buttons/labels and text area's to the container for the main frame
        container.add(labels[0]);
        container.add(labels[1]);
        container.add(new JScrollPane(partJList));
        container.add(new JScrollPane(partInfoTextArea));
        container.add(buttons[0]);
        container.add(buttons[1]);
        container.add(new JScrollPane(customerInfoTextArea));
        container.add(buttons[2]);
        container.add(labels[2]);
        container.add(labels[3]);
        container.add(new JScrollPane(orderJList));
        container.add(new JScrollPane(orderInfoTextArea));
        container.add(buttons[3]);
        container.add(buttons[4]);
        container.add(labels[4]);
        container.add(labels[5]);
        container.add(new JScrollPane(pastOrderJList));
        container.add(new JScrollPane(pastOrderInfoTextArea));
        container.add(buttons[5]);
        container.add(buttons[6]);
        container.add(buttons[7]);
 

        // Creates a new frame for the new customer login screen
        // Seperate panels are created for the labels, inputs and buttons that can be easily added to the frame.
        newCustomerFrame = new JFrame("New customer Login");
        newCustomerFrame.setResizable(false);
        Container newCustomerContainer;
        newCustomerFrame.setLayout(new GridLayout(3,1,5,5));
        newCustomerContainer = newCustomerFrame.getContentPane();

        // Creates label panel for new customer login screen
        // creates labels then adds them to the panel
        JPanel newCustomerLabelPanel = new JPanel();
        newCustomerLabelPanel.setLayout(new GridLayout(1, 4, 5, 5));
        JLabel newCustomerNameLabel = new JLabel("Name");
        JLabel newCustomerGenderLabel = new JLabel("Gender");
        JLabel newCustomerMobileLabel = new JLabel("Mobile Number");
        JLabel newCustomerAddressLabel = new JLabel("Delivery Address");
        newCustomerLabelPanel.add(newCustomerNameLabel);
        newCustomerLabelPanel.add(newCustomerGenderLabel);
        newCustomerLabelPanel.add(newCustomerMobileLabel);
        newCustomerLabelPanel.add(newCustomerAddressLabel);


        // Creating Customer Input Panel
        // Creates the 3 text area's and 1 combo box
        // Adds the text area's to an array to access
        // adds the components to the panel
        // creates the new customer input panel
        JPanel newCustomerInputPanel = new JPanel();
        newCustomerInputPanel.setLayout(new GridLayout(1, 4, 5, 5));
        JTextField newCustomerNameJTextField = new JTextField();
        String[] genderList = {"Male", "Female"};
        newCustomerGenderComboBox = new JComboBox<String>(genderList);
        JTextField newCustomerMobileJTextField = new JTextField();
        JTextField newCustomerAddressJTextField = new JTextField();
        newCustomerTextFields = new JTextField[3];
        newCustomerTextFields[0] = newCustomerNameJTextField;
        newCustomerTextFields[1] = newCustomerMobileJTextField;
        newCustomerTextFields[2] = newCustomerAddressJTextField;
        newCustomerInputPanel.add(newCustomerNameJTextField);
        newCustomerInputPanel.add(newCustomerGenderComboBox);
        newCustomerInputPanel.add(newCustomerMobileJTextField);
        newCustomerInputPanel.add(newCustomerAddressJTextField); 

        // creates the new customer button panel
        JPanel newCustomerButtonPanel = new JPanel();
        newCustomerButtonPanel.setLayout(new GridLayout(1,2, 10, 10));
        String newCustomerConfirmationButtonsNames[] = {"Login", "Cancel"};
        newCustomerConfirmationButtons = new JButton[newCustomerConfirmationButtonsNames.length];

        // creates Login/Cancel buttons and adds to the array to be easily accessed. adds action listener to buttons.
        // adds buttons to the button panel
        for(int i = 0; i < newCustomerConfirmationButtonsNames.length; i++){
           newCustomerConfirmationButtons[i] = new JButton(newCustomerConfirmationButtonsNames[i]);
           newCustomerConfirmationButtons[i].addActionListener(handler);
           newCustomerButtonPanel.add(newCustomerConfirmationButtons[i]);
        }

        // adds new customer labels, text area and button panels to the new customer login frame.
        // then sets the size of the frame.
        newCustomerFrame.add(newCustomerLabelPanel);
        newCustomerFrame.add(newCustomerInputPanel);
        newCustomerFrame.add(newCustomerButtonPanel);
        newCustomerFrame.setSize(500,100);
    }

    // method to disable main frame when user interacting with another frame
    public void disableFrame(){
        this.setEnabled(false);
    }

    // method to enable main frame when user finished interacting with another frame
    public void enableFrame(){
        this.setEnabled(true);
    }

    // Updates the order list model to update the order JList with parts in the currentOrder and order summary options.
    public void updateOrderList(){
        orderListModel.removeAllElements();
        orderListModel.addElement("Order Summary");

        for(ComputerPart part : orderingSystem.getCurrentOrder().getComputerParts()){
            orderListModel.addElement(part.getID());
        }
    } 

    // Updates the order list model to update the order JList with parts in the currentOrder and order summary options.
    public void updateHistoricalOrderList(){
        pastOrderJListModel.removeAllElements();

        // checks if current customer order list is not empty
        if(!orderingSystem.getCurrentCustomer().getOrders().isEmpty()){
            for(Order order : orderingSystem.getCurrentCustomer().getOrders()){
                pastOrderJListModel.addElement(order.getID());
            }
        }

    } 

    // private inner class used  as the ActionListener and ListSelectionListener
    private class OrderingSystemHandler implements ActionListener, ListSelectionListener {

        // Action listener method for buttons
        public void actionPerformed(ActionEvent event){

            boolean done = false;
            String input = "";
    
            // event handler for member customer login
            if (event.getSource() == buttons[0]){
                
                do{
                    try{
                        // creates option pane for user to enter their customer id and assigns it to input
                        input = JOptionPane.showInputDialog("Please input your Customer ID");

                        // breaks loop if cancel button is clicked in JOptionPane
                        if(input == null){
                            break;
                        }
                        // throws exception if no input when OK is clicked
                        else if(input.equals("")){
                            throw new InputMismatchException("No input detected");
                        }
                        // if input is not null or is not blank, will check if membercustomer exists and set as current customer if true and sets done to true
                        else if(!input.equals(null) || !input.equals("")){
                            if(!orderingSystem.memberCustomer(input)){
                                JOptionPane.showMessageDialog(null, "Sorry, your ID can't be found. Please login as a new customer.", "No customer found", JOptionPane.WARNING_MESSAGE);                   
                            }
                            else{
                                customerInfoTextArea.setText(""); // clears customer info text area
                                customerInfoTextArea.append(orderingSystem.getCurrentCustomer().toString()); // adds customer info to text area
                                orderListModel.removeAllElements(); // removes elements from the order list model as new order is created.
                                updateHistoricalOrderList();
                                done = true;
                            }
                        }
                        
                        
                    }
                    catch (InputMismatchException noInput){
                        JOptionPane.showMessageDialog(null, noInput, "Validation check", JOptionPane.WARNING_MESSAGE);
                    }
                    
                }while(!done);
            } // end of event handler for member customer login
    

            // event handler to clear then show new customer login window when Login as new customer selected
            if (event.getSource() == buttons[1]){
                for(JTextField field : newCustomerTextFields){
                    field.setText("");
                }
                newCustomerFrame.setVisible(true); // makes new customer login frame visible
                disableFrame(); // makes main JFrame un selectable
            } // end of event handler for new customer login window
    

            // event handler to close new customer login window when "cancel" is selected
            if(event.getSource() == newCustomerConfirmationButtons[1]){
                newCustomerFrame.dispose(); // disposes of frame when cancelled.
                enableFrame(); // enables main JFrame when new custmer login window is closed
            } // end of event handler for Cancel button on new customer login frame

            // checks if new customer login window is closed through the X in the top right of the frame
            // renables the main frame if so.
            newCustomerFrame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e){
                    enableFrame();
                }
            });
    

            // event handler for creating new customer using inputs in new customer login window.
            if(event.getSource() == newCustomerConfirmationButtons[0]){
                String checkInputString = "";
                String[] customerInfo = new String[4];
                boolean validationFailed = false;
                boolean formDone = false;
    
                // do while loop for new customer info pane. if validation fails at any point, breaks out of loop
                do{
                    // try/catch for customer name
                    try{
                        checkInputString = newCustomerTextFields[0].getText();
                        if(checkInputString.equals("") || checkInputString.equals(null)){
                            throw new InputMismatchException("No name entered");
                        } 
                        else {
                            customerInfo[0] = checkInputString; // collects user input where not null/empty in array
                        }
                    }
                    catch(InputMismatchException noInput){
                        JOptionPane.showMessageDialog(null, noInput, "Validation check", JOptionPane.WARNING_MESSAGE);
                        validationFailed = true;
                        break; // breaks out of loop if exception thrown
                    }

        

                    // try/catch for customer mobile
                    try{
                        checkInputString = newCustomerTextFields[1].getText();
                        if(checkInputString.equals("") || checkInputString.equals(null)){
                            throw new InputMismatchException("No Mobile number entered");
    
                        } 
                        else {
                            customerInfo[3] = checkInputString; // collects user input where not null/empty in array
                        }
                    }
                    catch(InputMismatchException noInput){
                        JOptionPane.showMessageDialog(null, noInput, "Validation check", JOptionPane.WARNING_MESSAGE);
                        validationFailed = true;
                        break; // breaks out of loop if exception thrown
                    }
        
                    // try/catch for customer Address
                    try{
                        checkInputString = newCustomerTextFields[2].getText();
                        if(checkInputString.equals("") || checkInputString.equals(null)){
                                throw new InputMismatchException("No address entered");
                        }    
                        else {
                            customerInfo[2] = checkInputString; // collects user input where not null/empty in array
                        }
                    }
                    catch(InputMismatchException noInput){
                        JOptionPane.showMessageDialog(null, noInput, "Validation check", JOptionPane.WARNING_MESSAGE);
                        validationFailed = true;
                        break;
                    }
                    
                   
                    formDone = true;
    
                }while(!formDone);
    
                // processes new customer info if validation is successful, shows new info in the main frame then closes
                if(!validationFailed){
                    customerInfo[1] = (String) newCustomerGenderComboBox.getSelectedItem(); // adds gender to customer info array
                    orderingSystem.newCustomer(customerInfo); // sends customer info to newCustoemr method in orderingSystem to create customer
                    customerInfoTextArea.setText(""); // clears customer info field
                    customerInfoTextArea.append(orderingSystem.getCurrentCustomer().toString()); // adds customer info to customer info field
                    newCustomerFrame.dispose(); // disposes login frame when completed
                    orderListModel.removeAllElements(); // clears order J List for after customer login.
                    pastOrderJListModel.removeAllElements(); // clears historical order list after login
                    enableFrame(); // enables main JFrame when login form is completed
                }
    
            } // end of new customer handler


            // event handler for Add to Order button
            if(event.getSource() == buttons[2]){
                if(orderingSystem.checkCustomer()){ // checks if customer is logged in
                    try{
                        if(partJList.isSelectionEmpty()){ // checks if part is selected
                            throw new InputMismatchException("No item selected");
                        }
                        else{
                            //orderJList.clearSelection();
                            orderingSystem.addToOrder(partJList.getSelectedIndex()); // gets selected index and sends to orderingSystem to add part
                            updateOrderList(); // updates the order list with the new part
                        }
                    }
                    catch (InputMismatchException noSelection){
                        JOptionPane.showMessageDialog(null, noSelection, "Item check", JOptionPane.WARNING_MESSAGE);
                    }

                } else JOptionPane.showMessageDialog(null, "No customer currently logged in.", "Invalid customer", JOptionPane.WARNING_MESSAGE);
            } // end of add to order handler


            // event handler for remove item from current order button
            if(event.getSource() == buttons[3]){
                if(orderingSystem.checkCustomer()){
                    try{
                        int selectedItemIndex = orderJList.getSelectedIndex();

                        if(orderJList.isSelectionEmpty()){ // checks if no selection
                            throw new InputMismatchException("No item selected");
                        } else if (selectedItemIndex == 0 || selectedItemIndex == -1){ // checks if Order Summary or out of bounds
                            throw new InputMismatchException("Invalid selection");
                        }
                        else{
                            // removes item based on selected index. it is always -1 as the Order Summary takes up first slot in JList.
                            orderingSystem.removeFromOrder(selectedItemIndex-1); 
                            updateOrderList();
                        }
                    }
                    catch (InputMismatchException selectionException){
                        JOptionPane.showMessageDialog(null, selectionException, "Item check", JOptionPane.WARNING_MESSAGE);
                    }

                } else JOptionPane.showMessageDialog(null, "No customer currently logged in.", "Invalid customer", JOptionPane.WARNING_MESSAGE);
            } // end of handler remove item from current order button


            // event handler for submit order
            if(event.getSource() == buttons[4]){
                if(orderingSystem.checkCustomer()){
                    if(orderingSystem.getCurrentOrder().getTotal() != 0){
                        orderingSystem.submitOrder();
                        updateHistoricalOrderList();
                        orderListModel.removeAllElements();
                        JOptionPane.showMessageDialog(null, "Your order has been submitted. Thanks for your purchase.");
                    } else JOptionPane.showMessageDialog(null, "No items in current order", "Order empty", JOptionPane.WARNING_MESSAGE);

                } else JOptionPane.showMessageDialog(null, "No customer currently logged in.", "Invalid customer", JOptionPane.WARNING_MESSAGE);
            } // end of handler for submit order


            // event handler for remove order button
            if(event.getSource() == buttons[5]){
                if(orderingSystem.checkCustomer()){
                    try{
                        int selectedOrderIndex = pastOrderJList.getSelectedIndex();

                        if(pastOrderJList.isSelectionEmpty()){
                            throw new InputMismatchException("No order selected");
                        }
                        else {
                            orderingSystem.cancelOrder(selectedOrderIndex);
                            updateHistoricalOrderList();
                        }
                    }
                    catch (InputMismatchException noSelection){
                        JOptionPane.showMessageDialog(null, noSelection, "Message", JOptionPane.WARNING_MESSAGE);
                    }
                } else JOptionPane.showMessageDialog(null, "No customer currently logged in.", "Invalid customer", JOptionPane.WARNING_MESSAGE);
            } // end of handler for remove order button


            // event handler for Export Order button
            if(event.getSource() == buttons[6]){
                if(orderingSystem.checkCustomer()){ // checks to see if a customer is logged in
                    if(!orderingSystem.getCurrentCustomer().getOrders().isEmpty()){ // checks to see if customer has submitted orders
                        orderingSystem.exportOrders(); // method within orderingSystem to export orders to .txt
                        JOptionPane.showMessageDialog(null, "Export of order(s) successful"); // shows successful message when export completed
                    } else JOptionPane.showMessageDialog(null, "No submitted orders found.", "No orders", JOptionPane.WARNING_MESSAGE);
                } else JOptionPane.showMessageDialog(null, "No customer currently logged in.", "Invalid customer", JOptionPane.WARNING_MESSAGE);
            } // end of handler for export order button


            // event handler for exit system button
            if(event.getSource() == buttons[7]){
                JOptionPane.showMessageDialog(null, "Thanks for using the Computer Shop Ordering System, see you next time!");
                System.exit(0);
            }

    
        }

        // List selection listener
        public void valueChanged(ListSelectionEvent event){

            // part list handler
            if(event.getSource() == partJList){
                int index = partJList.getSelectedIndex();
                // if selection is not -1 which means an item is selected
                if(index != -1){
                    partInfoTextArea.setText(orderingSystem.getComputerParts().get(index).toString());
                } else partInfoTextArea.setText(""); // will clear text if no item is selected
                
            } // end of part list handler


            // order part list handler
            if(event.getSource() == orderJList){
                int index = orderJList.getSelectedIndex();
                // index 0 always = the Current Order list selection
                if(index == 0){
                    orderInfoTextArea.setText(orderingSystem.getCurrentOrder().toString());
                }
                // index -1 for when the JList item is unselected
                else if (index == -1) {
                    orderInfoTextArea.setText("");
                }
                // if index is not 0 or -1 then an item is selected.
                else {
                    Order currentOrder = orderingSystem.getCurrentOrder(); // gets current order
                    String partInfo = currentOrder.getComputerParts().get((index-1)).toString(); // gets part info of selected part
                    partInfo += currentOrder.getNonCompatibleParts(currentOrder.getComputerParts().get((index-1))); // adds non compatible string for selected part
                    orderInfoTextArea.setText(partInfo); // shows partInfo in the order info text area
                }

            } // end of order part list handler


            // historical order list handler
            if(event.getSource() == pastOrderJList){
                int orderIndex = pastOrderJList.getSelectedIndex();

                // if not unselected, creates the orderr info string with the list of computer parts and updates the
                // past order info text area otherwise clears the text area if nothing is selected.
                if(orderIndex != -1){
                    Order selectedOrder = orderingSystem.getCurrentCustomer().getOrders().get(orderIndex);
                    String orderInfo = "Order Information\n";

                    for(ComputerPart part : selectedOrder.getComputerParts()){
                        orderInfo += part + "\n\n";
                    }

                    orderInfo += selectedOrder;
                    pastOrderInfoTextArea.setText(orderInfo);

                } else pastOrderInfoTextArea.setText("");

            } // end of historical order list handler
            
        }
    
    }


}

// Ordering system class which is used to manage the current customer and current order.
// This class also holds the computer parts and existing customers.
// initialised via the main class
class OrderingSystem implements Serializable {

    // ArrayLists created to store objects.
    // currentCustomer/currentOrder are used to keep track of the current customer using the program and current order
    private ArrayList<Customer> customers = new ArrayList<Customer>();
    private ArrayList<ComputerPart> parts = new ArrayList<ComputerPart>();
    private Customer currentCustomer;
    private Order currentOrder;

    // method used to add parts to array list
    public void addComputerPart(ComputerPart newPart){
        parts.add(newPart);
    }

    //method used to add customers to customers array
    public void addCustomer(Customer customer){
        customers.add(customer);
    }

    // method used to return parts array list
    public ArrayList<ComputerPart> getComputerParts(){
        return parts;
    }

    // method used to return customers array list
    public ArrayList<Customer> getCustomers(){
        return customers;
    }

    // method used to set current customer
    public void setCurrentCustomer(Customer currentCustomer){
        this.currentCustomer = currentCustomer;
    }

    // method used to set current order
    public void setCurrentOrder(Order currentOrder){
        this.currentOrder = currentOrder;
    }

    // method used to return current customer
    public Customer getCurrentCustomer(){
        return this.currentCustomer;
    }

    // method used to return current order
    public Order getCurrentOrder(){
        return this.currentOrder;
    }

    // method used to return string array for all parts in the parts array list.
    // used for the JList in the main class 
    public String[] getPartList(){
        
        String[] partList = new String[parts.size()];

        int index = 0;
        for (ComputerPart c : parts){
            partList[index] = c.getID();
            index++;
        }

        return partList;
    }

    // Method used to initialise the shop where .ser file cannot be loaded, creates objects and adds them to the respective arraylist.
    // creates objects of customers and computer parts.
    public void initialiseShop(){
        
        addComputerPart(new IntelCPU("i5", "9600K", 323));
        addComputerPart(new IntelCPU("i7", "9700K", 462));
        addComputerPart(new IntelCPU("i7", "9700F", 396));
        addComputerPart(new IntelCPU("i9", "9900K", 591));
        addComputerPart(new AMDCPU("4", "Ryzen 2200", 200));
        addComputerPart(new AMDCPU("6", "Ryzen 3600", 310));
        addComputerPart(new AMDCPU("8", "Ryzen 3700", 489));
        addComputerPart(new AMDCPU("8", "Ryzen 5800", 669));


        addComputerPart(new IntelMotherboard("Gigabyte", "H81M-DS2", 129));
        addComputerPart(new IntelMotherboard("Asus", "J40051-C", 169));
        addComputerPart(new IntelMotherboard("Msi", "Mpg-2390", 225));
        addComputerPart(new IntelMotherboard("Gigabyte", "Z490", 471));
        addComputerPart(new AMDMotherboard("Gigabyte", "B-450", 117));
        addComputerPart(new AMDMotherboard("Asus", "A320I", 128));
        addComputerPart(new AMDMotherboard("Msi", "B450", 232));
        addComputerPart(new AMDMotherboard("Gigabyte", "X570S", 679));


        addComputerPart(new Memory("DDR3", "8G", "Kingston", "KCP316ND8", 116));
        addComputerPart(new Memory("DDR3", "16G",  "ADATA", "AX4U360038G18", 189));
        addComputerPart(new Memory("DDR3", "8G",  "G.Skill", "F3-10666CL9D", 96));
        addComputerPart(new Memory("DDR4", "8G", "Kingston", "KCP426SS8", 93));
        addComputerPart(new Memory("DDR4", "16G", "G.Skill", "F4-2666C18S", 158));
        addComputerPart(new Memory("DDR4", "32G", "Crucial", "CT32G4SFD832A", 259));


        addComputerPart(new IntelGraphicsCard("Gigabyte", "GeForce RTX 3070", 1999));
        addComputerPart(new IntelGraphicsCard("Asus", "GeForce RTX 3070", 1899));
        addComputerPart(new IntelGraphicsCard("Msi", "GeForce RTX 3080", 3099));
        addComputerPart(new AMDGraphicsCard("Gigabyte", "Radeon RX 6900", 3699));
        addComputerPart(new AMDGraphicsCard("Asus", "Radeon RX 6900", 3199));
        addComputerPart(new AMDGraphicsCard("Msi", "Radeon RX 6900", 2699));


        addComputerPart(new Monitor("Acer", "24", "K242HYLB", 194));
        addComputerPart(new Monitor("LG", "32", "32QN600", 506));
        addComputerPart(new Monitor("Asus", "16", "MB16ACZ", 429));
        addComputerPart(new Monitor("Msi", "27", "MP271QP", 399));
        addComputerPart(new Monitor("BenQ", "32", "PD3200Q", 653));
        addComputerPart(new Monitor("Philips", "27", "272M8CZ", 289));

        addCustomer(new MemberCustomer("C000001", "Amy Bell", "Female", "No. 1, NoName Street, NeverLand, 0000.", "04211111"));
        addCustomer(new MemberCustomer("C000002", "Bob Brown", "Male", "No. 2, NoName Street, NeverLand, 0000.", "04212222"));
        addCustomer(new MemberCustomer("C000003", "Cindy Ma", "Female", "No. 3, NoName Street, NeverLand, 0000.", "04213333"));
        addCustomer(new MemberCustomer("C000004", "David Hintz", "Male", "No. 4, NoName Street, NeverLand, 0000.", "04214444"));
        addCustomer(new MemberCustomer("C000005", "Rex White", "Male", "No. 5, NoName Street, NeverLand, 0000.", "04215555"));
       

    }

    // Method used to validate existing customers Customer ID and check to see if they exist in the customers arraylist.
    // If the user exists then it logs them in and assigns them to currentCustomer 
    // it then checks if they are a memberCustomer or new customer and creates either a 
    // discount order or regular order which is assigned to currentOrder.
    public boolean memberCustomer(String cNumber){
        
        Boolean customerFound = false;

        // for loop checks the entered customer ID with the list.
        // I have also made sure that if a new customer creates and account then tries to re-log in 
        // using the existing/member customer menu, that it creates the correct order based on the customer type and avoids crashing.
        for (Customer c : customers){
            if (c.getCustomerID().equals(cNumber)){
                customerFound = true;
                currentCustomer = c;
                if (currentCustomer instanceof MemberCustomer){
                    MemberCustomer discountCustomer = (MemberCustomer) c;
                    currentOrder = new DiscountOrder(discountCustomer.getDISCOUNT());
                } else currentOrder = new Order();
            }
        }

        return customerFound;

    }

    // Method used to create a new customer account and assign it to currentCustomer
    // Once new user is created they are added to customers arraylist and a new Order is created
    // and assigned to currentOrder.
    public void newCustomer(String[] customerDetails){
       
        Customer newCustomer = new Customer(customerDetails[0], customerDetails[1], customerDetails[2], customerDetails[3]);
        addCustomer(newCustomer);

        currentCustomer = newCustomer;

        currentOrder = new Order();
    }

    // method used to check if a customer is logged into the system.
    public boolean checkCustomer(){

        boolean checkCustomer = false;

        if(currentCustomer != null){
            checkCustomer = true;
        }
        return checkCustomer;
        
    }

    // method used to add selected part to currentOrder parts array list
    public void addToOrder(int partIndex){
        currentOrder.addPart(parts.get(partIndex).copy());
    }


    // Method used to view all parts in the current order, user can then choose to remove parts.
    public void removeFromOrder(int partIndex){
        currentOrder.removePart(partIndex);
    }

    // Method used to submit the order and add it to currentCustomers Orders arraylist.
    // when order is submitted a new Order is created based on customer type.
    public void submitOrder(){

        currentCustomer.addOrder(currentOrder);

        if (currentCustomer instanceof MemberCustomer){
            MemberCustomer existingCustomer = (MemberCustomer) currentCustomer;
            currentOrder = new DiscountOrder(existingCustomer.getDISCOUNT());
        } else currentOrder = new Order();
    }

    // Method used to cancel selected order and remove it from currentCustomers order array list
    public void cancelOrder(int orderIndex){
        currentCustomer.removeOrder(orderIndex);
    }

    // Method used to export customer orders to .txt file
    public void exportOrders() {
        
        try{
            Formatter output = new Formatter(currentCustomer.getCustomerID() + ".txt");
            output.format("%s", currentCustomer);
            for(Order order : currentCustomer.getOrders()){
                for(ComputerPart part : order.getComputerParts()){
                    output.format("\n\n%s", part);
                }
                output.format("\n\n%s", order);
            }
            output.close();
        }
        catch(SecurityException noWritePermission){
            JOptionPane.showMessageDialog(null, noWritePermission, "Write error", JOptionPane.WARNING_MESSAGE);
        }
        catch(FormatterClosedException writingError){
            JOptionPane.showMessageDialog(null, writingError, "File close error", JOptionPane.WARNING_MESSAGE);
        }
        catch(NoSuchElementException noElement){
            JOptionPane.showMessageDialog(null, noElement, "Invalid input", JOptionPane.WARNING_MESSAGE);
        }
        catch(FileNotFoundException noFile){
            JOptionPane.showMessageDialog(null, noFile, "No file error", JOptionPane.WARNING_MESSAGE);
        }
    }

}

// Order class used to hold the objects of computer part class that the user has added to their order.
// it also calculates and holds the total price of the order.
class Order implements Serializable{

    protected final String ORDER_ID;
    protected ArrayList<ComputerPart> parts;

    private double totalPrice;

    // constructor for Order, creates random order ID
    public Order (){

        Random random = new Random();
        int max = 99999;
        int min = 10000;
        int id = random.nextInt(max - min) + min;

        this.ORDER_ID = "O" + id;
        parts = new ArrayList<ComputerPart>();

    }

    public String getID(){
        return this.ORDER_ID;
    }

    public double getTotal(){
        calculateTotal();
        return this.totalPrice;
    }

    public void addPart(ComputerPart newPart){
        parts.add(newPart);
    }

    public void removePart(ComputerPart part){
        parts.remove(part);
    }

    public void removePart(int partIndex){
        parts.remove(partIndex);
    }

    // method used to return a String of all nonCompatible parts in the order
    public String getNonCompatibleParts(ComputerPart part){
        
        String result = "";
        
        String incompatibleParts = "";
        boolean nonCompatible = false;
        
        // For loop checks compatibility, marks nonCompatible to true if any noncompatible items are found.
        // nonCompatible triggers the if statement below this for loop.
        // non compatible item ID's are stored in the incompatibleParts String.
        for(ComputerPart checkPart : parts){
            if(!part.isCompatible(checkPart)){
                incompatibleParts += checkPart.getID() + ",";
                nonCompatible = true;
            }
        }
        
        // Checks if the current part in the loop is non compatible with any parts in the order by using nonCompatible boolean
        // the incompatibleParts string is then stored in the incompatiblePartsList so we can easily iterate through
        // this array allows us to check when we're at the end of the array so we don't add extra comma's.
        // all non compatible parts are added to the result String after the checked part.
        if(nonCompatible){
            result += "\nNon compatible with: ";
            String[] incompatiblePartsList = incompatibleParts.split(",");
            int counter = 0;
            for (String partID : incompatiblePartsList){
                counter++;
                if(counter == incompatiblePartsList.length){
                    result += partID;
                } else result += partID + ", ";
            }
        }
            
        return result;
        
    }

    // to string method used for Order.
    // loops through the order parts list and adds them to the result string
    public String toString(){

        return "Order: " + ORDER_ID + String.format("\nTotal Price: $%.2f", getTotal());
    } 

    // This method is used to get the total of the order based on which parts have been added to the order.
    // before adding it to the totalPrice, it makes sure that the object exists to avoid exceptions. It does this check for each object.
    public void calculateTotal(){

        this.totalPrice = 0;

        for(ComputerPart part : parts){
            totalPrice += part.getPrice();
        }

    }

    public ArrayList<ComputerPart> getComputerParts(){
        return parts;
    }

    // method used to return a String array of the current parts in the order
    public String[] getPartList(){

        String[] partList = new String[parts.size()];

        int index = 0;
        for (ComputerPart c : parts){
            partList[index] = c.getID();
            index++;
        }

        return partList;
    }


}

// DiscountOrder class which extends order class and is used to create DiscountOrders for memberCustomers
// Discount order holds a discount double which is used to provide a discount to the order of memberCustomers.
class DiscountOrder extends Order {
    private double discount;

    public DiscountOrder(double discount){
        super();
        this.discount = discount;
    }

    public DiscountOrder(){
        super();
        this.discount = 0;
    }

    public double getDiscount(){
        return this.discount;
    }

    // method calculates the discount price of the order
    public double calculateDiscount(){
        return ((1.00-discount)*getTotal());
    }

    // to string method calls the super toString then adds the discounted information
    public String toString(){

        String result = super.toString();
        result += String.format("\nMember Customer Discount: $%.2f\nTotal Price after the Discount: $%.2f", (getTotal()-calculateDiscount()), calculateDiscount());
        return result; 
    }
}

// Customer class used to store customer data as well as their past Orders in the arraylist
class Customer implements Serializable {

    protected final String CUSTOMER_ID;
    protected String name, gender, address, mobile;
    protected ArrayList<Order> orders = new ArrayList<Order>();

    // customer constructor where ID is provided. i.e. existing customers
    public Customer (String id, String name, String gender, String address, String mobile){

        this.CUSTOMER_ID = id;
        this.name = name;
        this.gender = gender;
        this.address = address;
        this.mobile = mobile;
    
    }

    // customer constructor with random Customer ID generation. i.e. for new customers
    public Customer (String name, String gender, String address, String mobile){

        Random random = new Random();
        int max = 99999;
        int min = 10000;
        int id = random.nextInt(max - min) + min;

        this.CUSTOMER_ID = "C" + id;
        this.name = name;
        this.gender = gender;
        this.address = address;
        this.mobile = mobile;

    }

    public Customer(){
        this.CUSTOMER_ID = "";
        this.name = "";
        this.gender = "";
        this.address = "";
        this.mobile = "";
    }


    // Customer class uses get/set methods listed below.
    public String getCustomerID(){
        return this.CUSTOMER_ID;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String newName){
        this.name = newName;
    }

    public String getGender(){
        return this.gender;
    }

    public void setGender(String newGender){
        this.gender = newGender;
    }

    public String getAddress(){
        return this.address;
    }

    public void setAddress(String newAddress){
        this.address = newAddress;
    }

    public String getMobile(){
        return this.mobile;
    }

    public void setMobile(String newMobile){
        this.mobile = newMobile;
    }

    // method adds their submitted order to their orderlist
    public void addOrder(Order submittedOrder){
        orders.add(submittedOrder);
    }

    // method used to remove selected order from orders array list
    public void removeOrder(int index){
        orders.remove(index);
    }

    // returns customers orders arraylist
    public ArrayList<Order> getOrders(){
        return orders;
    }

    // toString method used to return customer information as a string.
    public String toString(){

        String result = String.format("Customer Information\nCustomer ID: %s\nName: %s\nGender: %s\nMobile: %s\nAddress: %s", 
        this.CUSTOMER_ID, this.name, this.gender, this.mobile, this.address);

        return result;
    }
}

// MemberCustomer class extends customer class which is used to hold their discount rate.
class MemberCustomer extends Customer {

    // DISCOUNT is a constant that always equals 0.05.
    // this can be changed in the future so that this value is assigned in the constructor instead.
    private final double DISCOUNT = 0.05;

    // member customer constructor
    public MemberCustomer (String id, String name, String gender, String address, String mobile){
        super(id, name, gender, address, mobile);
    }

    // default constructor
    public MemberCustomer(){
        super();
    }

    public double getDISCOUNT(){
        return this.DISCOUNT;
    }

    // toString calls super toString then adds the discount rate.
    public String toString(){
        return super.toString() + String.format("\nDiscount: %.2f", (DISCOUNT * 100)) + "%";
    }
}

// compatible interface used to implement the isCompatible method for all ComputerParts
interface Compatible{
    public boolean isCompatible(ComputerPart part);
}

// Abstract class ComputerPart used as a super class to a number of sub classes.
// main class object for parts in the system which holds base part information.
// implements compatible interface to force all computer parts to implement isCompatible method.
abstract class ComputerPart implements Compatible, Serializable {
    
    protected final String ID, BRAND, MODEL;
    protected double price;


    // ComputerPart constructor assigns random ID on creation
    public ComputerPart(String prefix, String brand, String model, double price){

        Random random = new Random();
        int max = 99999;
        int min = 10000;


        this.ID = prefix + (random.nextInt(max - min) + min);
        this.BRAND = brand;
        this.MODEL = model;
        this.price = price;
    }

    // Copy constructor
    public ComputerPart (ComputerPart source){
        this.ID = source.ID;
        this.BRAND = source.BRAND;
        this.MODEL = source.MODEL;
        this.price = source.price;
    }

    // default constructor
    public ComputerPart(){
        this.ID = "";
        this.BRAND = "";
        this.MODEL = "";
        this.price = 0;
    }
    

    public String getID(){
        return this.ID;
    }

    public double getPrice(){
        return this.price;
    }

    public void setPrice(double newPrice){
        this.price = newPrice;
    }

    public String getBrand(){
        return this.BRAND;
    }

    
    public String getModel(){
        return this.MODEL;
    }

    // abstract method entered here to allow sub classes to override it.
    public abstract ComputerPart copy();

    public String toString(){
        return String.format("Part ID: %s\nBrand: %s\nModel: %s\nPrice: $%.2f", ID, BRAND, MODEL, price);
    }
    
}

// This class is used to store data for the CPU object
// Class is abstract and used as a base class for the 2 sub classes of CPU
// class extends the ComputerPart class
abstract class CPU extends ComputerPart{

    protected final String CORE;

    // CPU constructors
    public CPU (String prefix, String brand, String core, String model, double price){

        super((prefix+"CPU"), brand, model, price);
        this.CORE = core;
    }

    public CPU (String core, String model, double price){
        super("CPU", "N/A", model, price);
        this.CORE = core;
    }

    // Copy constructor
    public CPU (CPU source){
        super((ComputerPart) source);
        this.CORE = source.CORE;
    }


    // default constructor
    public CPU(){
        super();
        this.CORE = "";

    }
   
    public String getCore(){
        return this.CORE;
    }

    // toString method used to return CPU data as a string.
    @Override
    public String toString(){

        String result = super.toString() + String.format("\nCore: %s", CORE);
        return result;
    }


}

// IntelCPU class which is a subclass of CPU. used to hold the SOCKET type.
class IntelCPU extends CPU {

    private final String SOCKET;

    // IntelCPU constructor
    public IntelCPU(String core, String model, double price){
        super("INT", "Intel", core, model, price);
        this.SOCKET = "Intel";
    }

    // Copy constructor
    public IntelCPU (IntelCPU source){
        super((CPU) source);
        this.SOCKET = source.SOCKET;
    }

    // default constructor
    public IntelCPU(){
        super();
        this.SOCKET = "";
    }

    public String getSocket(){
        return this.SOCKET;
    }

    // copy method is used to call on the copy constructor of 'this' object
    // overrides the copy method of ComputerPart as object knows of what type it is
    // this makes it easier to call for a copy of a subclass and return a ComputerPart reference of subclass object
    @Override
    public ComputerPart copy(){
        return new IntelCPU(this);
    }

    @Override
    public String toString(){
        String result = super.toString() + String.format("\nSocket: %s", SOCKET);
        return result;
    }

    // isCompatible used to check compatibility of the entered 'part' with the current object
    // returns true if the part is compatible and flase if not compatible.
    public boolean isCompatible(ComputerPart part){

        if (part instanceof AMDGraphicsCard || part instanceof AMDMotherboard || part instanceof AMDCPU){
            return false;
        } else return true;
        
    }



}

// AMDCPU class which is a subclass of CPU. used to hold the SOCKET type.
class AMDCPU extends CPU {

    private final String SOCKET;

    // AMDCPU constructor
    public AMDCPU(String core, String model, double price){
        super("AMD", "AMD", core, model, price);
        this.SOCKET = "AMD";
    }

    // copy constructor
    public AMDCPU (AMDCPU source){
        super((CPU) source);
        this.SOCKET = source.SOCKET;
    }

    // default cosntructor
    public AMDCPU(){
        super();
        this.SOCKET = "";
    }

    public String getSocket(){
        return this.SOCKET;
    }

    // copy method is used to call on the copy constructor of 'this' object
    // overrides the copy method of ComputerPart as object knows of what type it is
    // this makes it easier to call for a copy of a subclass and return a ComputerPart reference of subclass object
    @Override
    public ComputerPart copy(){
        return new AMDCPU(this);
    }

    @Override
    public String toString(){
        String result = super.toString() + String.format("\nSocket: %s", SOCKET);
        return result;
    }

    // isCompatible used to check compatibility of the entered 'part' with the current object
    // returns true if the part is compatible and flase if not compatible.
    public boolean isCompatible(ComputerPart part){

        if (part instanceof IntelGraphicsCard || part instanceof IntelMotherboard || part instanceof IntelCPU){
            return false;
        } else return true;
    }


}

// This class is used to store data for the motherboard object
// is an abstract class which extends ComputerPart
// used as the superclass to AMD/Intel Motherboard classes
abstract class Motherboard extends ComputerPart {

    // Motherboard constructor
    public Motherboard(String prefix, String brand, String model, double price){
        super((prefix+"MOT"), brand, model, price);

    }

    public Motherboard(String brand, String model, double price){
        super("MOT", brand, model, price);
    }

    // copy constructor
    public Motherboard (Motherboard source){
        super((ComputerPart) source);
    }

    // default constructor
    public Motherboard (){
        super();
        
    }
    
}

// IntelMotherboard class which is a subclass of Motherboard. used to hold the SOCKET type.
class IntelMotherboard extends Motherboard {
    private final String SOCKET;

    // IntelMotherboard constructor
    public IntelMotherboard(String brand, String model, double price){
        super("INT", brand, model, price);
        this.SOCKET = "Intel";

    }
    
    // copy constructor
    public IntelMotherboard (IntelMotherboard source){
        super((Motherboard) source);
        this.SOCKET = source.SOCKET;
    }

    // default constructor
    public IntelMotherboard(){
        super();
        this.SOCKET = "";
    }

    public String getSocket(){
        return this.SOCKET;
    }

    // copy method is used to call on the copy constructor of 'this' object
    // overrides the copy method of ComputerPart as object knows of what type it is
    // this makes it easier to call for a copy of a subclass and return a ComputerPart reference of subclass object
    @Override            
    public ComputerPart copy(){
        return new IntelMotherboard(this);
    }

    // toString method used to return motherboard data as a string.
    @Override
    public String toString(){

        return super.toString() + String.format("\nSocket: %s", SOCKET);
    }

    // isCompatible used to check compatibility of the entered 'part' with the current object
    // returns true if the part is compatible and flase if not compatible.
    public boolean isCompatible(ComputerPart part){

        if (part instanceof AMDGraphicsCard || part instanceof AMDMotherboard || part instanceof AMDCPU){
            return false;
        } else return true;
    }



}

// AMDMotherboard class which is a subclass of Motherboard. used to hold the SOCKET type.
class AMDMotherboard extends Motherboard {
    private final String SOCKET;

    // AMDMotherboard constructor
    public AMDMotherboard(String brand, String model, double price){
        super("AMD", brand, model, price);
        this.SOCKET = "AMD";

    }
    
    // copy constructor
    public AMDMotherboard (AMDMotherboard source){
        super((Motherboard) source);
        this.SOCKET = source.SOCKET;
    }

    // default constructor
    public AMDMotherboard(){
        super();
        this.SOCKET = "";
    }

    public String getSocket(){
        return this.SOCKET;
    }

    // copy method is used to call on the copy constructor of 'this' object
    // overrides the copy method of ComputerPart as object knows of what type it is
    // this makes it easier to call for a copy of a subclass and return a ComputerPart reference of subclass object
    @Override        
    public ComputerPart copy(){
        return new AMDMotherboard(this);
    }

    // toString method used to return motherboard data as a string.
    @Override
    public String toString(){
        return super.toString() + String.format("\nSocket: %s", SOCKET);
    }

    // isCompatible used to check compatibility of the entered 'part' with the current object
    // returns true if the part is compatible and flase if not compatible.
    public boolean isCompatible(ComputerPart part){

        if (part instanceof IntelGraphicsCard || part instanceof IntelMotherboard || part instanceof IntelCPU){
            return false;
        } else return true;
    }



}


// This class is used to store data for the Memory object
// class extends ComputerPart
class Memory extends ComputerPart {

    private final String SOCKET, SIZE;

    // Memory constructor
    public Memory (String socket,  String size, String brand, String model, double price){
        super("MEM", brand, model, price);
        this.SOCKET = socket;
        this.SIZE = size;
    }

    // copy constructor
    public Memory (Memory source){
        super((ComputerPart) source);
        this.SOCKET = source.SOCKET;
        this.SIZE = source.SIZE;
    }

    // default constructor
    public Memory (){
        super();
        this.SOCKET = "";
        this.SIZE = "";
    }

    public String getSocket(){
        return this.SOCKET;
    }


    public String getSize(){
        return this.SIZE;
    }

    // copy method is used to call on the copy constructor of 'this' object
    // overrides the copy method of ComputerPart as object knows of what type it is
    // this makes it easier to call for a copy of a subclass and return a ComputerPart reference of subclass object
    @Override    
    public ComputerPart copy(){
        return new Memory(this);
    }


    // toString method used to return Memory data as a string.
    @Override
    public String toString(){

        return super.toString() + String.format("\nSocket: %s\nSize: %s", SOCKET, SIZE);
    }

    // isCompatible method to check if part is compatible with current object
    // Memory is compatible with all other objects/parts so always returns true
    public boolean isCompatible(ComputerPart part){
        return true;
    }

    
}

// This class is used to store data for the graphics card object
// used as superclass for Intel/AMD Gpu's
abstract class GraphicsCard extends ComputerPart {

    // graphics card constructor
    public GraphicsCard (String prefix, String brand, String model, double price){
        super((prefix+"GRA"), brand, model, price);
    }

    public GraphicsCard (String brand, String model, double price){
        super("GRA", brand, model, price);
    }

    // copy constructor
    public GraphicsCard (GraphicsCard source){
        super((ComputerPart) source);
    }

    // default constructor
    public GraphicsCard (){
        super();
    }
    
}

// IntelGraphics class which is a subclass of GraphicsCard. used to hold the SOCKET type.
class IntelGraphicsCard extends GraphicsCard{

    private final String SOCKET;

    // Intel GPU constructor
    public IntelGraphicsCard(String brand, String model, double price){
        super("INT", brand, model, price);
        this.SOCKET = "Intel";
    }

    // copy constructor
    public IntelGraphicsCard (IntelGraphicsCard source){
        super((GraphicsCard) source);
        this.SOCKET = source.SOCKET;
    }

    //default constructor
    public IntelGraphicsCard(){
        super();
        this.SOCKET = "";
    }

    public String getSocket(){
        return this.SOCKET;
    }

    // copy method is used to call on the copy constructor of 'this' object
    // overrides the copy method of ComputerPart as object knows of what type it is
    // this makes it easier to call for a copy of a subclass and return a ComputerPart reference of subclass object
    @Override
    public ComputerPart copy(){
        return new IntelGraphicsCard(this);
    }

    @Override
    public String toString(){
        return super.toString() + String.format("\nSocket: %s", SOCKET);
    }

    // isCompatible used to check compatibility of the entered 'part' with the current object
    // returns true if the part is compatible and flase if not compatible.
    public boolean isCompatible(ComputerPart part){

        if (part instanceof AMDGraphicsCard || part instanceof AMDMotherboard || part instanceof AMDCPU){
            return false;
        } else return true;
    }


}

// AMDGraphicsCard class which is a subclass of GraphicsCard. used to hold the SOCKET type.
class AMDGraphicsCard extends GraphicsCard {

    private final String SOCKET;

    // AMD GPU constructor
    public AMDGraphicsCard(String brand, String model, double price){
        super("AMD", brand, model, price);
        this.SOCKET = "AMD";
    }

    // copy constructor
    public AMDGraphicsCard (AMDGraphicsCard source){
        super((GraphicsCard) source);
        this.SOCKET = source.SOCKET;
    }

    // default constructor
    public AMDGraphicsCard(){
        super();
        this.SOCKET = "";
    }

    public String getSocket(){
        return this.SOCKET;
    }

    // copy method is used to call on the copy constructor of 'this' object
    // overrides the copy method of ComputerPart as object knows of what type it is
    // this makes it easier to call for a copy of a subclass and return a ComputerPart reference of subclass object
    @Override
    public ComputerPart copy(){
        return new AMDGraphicsCard(this);
    }

    @Override
    public String toString(){
        return super.toString() + String.format("\nSocket: %s", SOCKET);
    }

    // isCompatible used to check compatibility of the entered 'part' with the current object
    // returns true if the part is compatible and flase if not compatible.
    public boolean isCompatible(ComputerPart part){

        if (part instanceof IntelGraphicsCard || part instanceof IntelMotherboard || part instanceof IntelCPU){
            return false;
        } else return true;

    }


}

// This class is used to store data for the monitor object
// extends ComputerPart class
class Monitor extends ComputerPart{

    private final String SIZE;

    // monitor constructor
    public Monitor (String brand, String size, String model, double price){
        super("MON", brand, model, price);
        this.SIZE = size;
    }

    // copy constructor
    public Monitor (Monitor source){
        super((ComputerPart) source);
        this.SIZE = source.SIZE;
    }

    // default constructor
    public Monitor (){
        super();
        this.SIZE = "";
    }

    // copy method is used to call on the copy constructor of 'this' object
    // overrides the copy method of ComputerPart as object knows of what type it is
    // this makes it easier to call for a copy of a subclass and return a ComputerPart reference of subclass object
    @Override
    public ComputerPart copy(){
        return new Monitor(this);
    }

    public String getSize(){
        return this.SIZE;
    }

    // toString method used to return monitor data as a string.
    @Override
    public String toString(){ 
        return super.toString() + String.format("\nSize: %s", SIZE);
    }

    // isCompatible method to check if part is compatible with current object
    // Monitor is compatible with all other objects/parts so always returns true
    public boolean isCompatible(ComputerPart part){
        return true;
    }

}

// Class which is used to Serialize/Deserialize the OrderingSystem.
class CreateOrderingSystem {
    private OrderingSystem orderSystem;

    // constructor used to create .ser file, takes the OrderingSystem and file name to create the .ser file
    public CreateOrderingSystem(OrderingSystem system, String filename){

        orderSystem = system;

        try{
            FileOutputStream file = new FileOutputStream(filename);

            ObjectOutputStream outputStream = new ObjectOutputStream(file);

            outputStream.writeObject(orderSystem);

            outputStream.close();
            file.close();
        }
        catch(IOException e){
            System.err.println(e + "Order system creation unsuccessful");
        }
    }

    // constructor used to load .ser file. uses filename to find and load file
    public CreateOrderingSystem (String filename) throws IOException, ClassNotFoundException{

        try{
            FileInputStream file = new FileInputStream(filename);

            ObjectInputStream inputStream = new ObjectInputStream(file);

            orderSystem = (OrderingSystem) inputStream.readObject();

            inputStream.close();
            file.close();
        }
        catch (IOException exception){
            throw new IOException(exception + ".ser File failed to load, initialising shop and creating .ser file");
        }
        catch (ClassNotFoundException exception){
            throw new ClassNotFoundException(exception + ".ser File not found, initialising shop and creating .ser file");
        }
    }

    // returns loaded ordering system
    public OrderingSystem getOrderingSystem(){
        return orderSystem;
    }
}