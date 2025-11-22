package view.login.componet;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import javax.swing.*;

import controller.AuthenticationController;
import model.dao.IUserDAO;
import model.services.AuthenticationService;
import net.miginfocom.swing.MigLayout;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;
import org.jdesktop.animation.timing.TimingTargetAdapter;
import view.login.fiels.Button;
import view.login.fiels.MyPasswordField;
import view.login.fiels.MyTextField;

public class Loginview extends JFrame {

    private final DecimalFormat df = new DecimalFormat("##0.###", DecimalFormatSymbols.getInstance(Locale.US));
    private MigLayout layout;
    private PanelCover cover;
    private PanelLoading loading;
    private boolean isLogin;
    private final double addSize = 30;
    private final double coverSize = 40;
    private final double loginSize = 60;
    private JLayeredPane bg;
    private MyTextField emailField;
    private MyTextField txtFirstName, txtLastName, txtEmail, txtBi, txtPassport, txtNuit, txtPhone, txtAddress;
    private MyPasswordField passwordField;
    private MyPasswordField txtPassword;
    private Button loginButton;
    private Button sigupButton;
    private JPanel login = new JPanel();
    private JPanel register = new JPanel();
    private JLayeredPane logReg;

    public Loginview() {
        initComponents();
        init();
    }

    private void init() {
        layout = new MigLayout("fill, insets 0");
        cover = new PanelCover();
        loading = new PanelLoading();
        loginReg();
        TimingTarget target = new TimingTargetAdapter() {
            @Override
            public void timingEvent(float fraction) {
                double fractionCover;
                double fractionLogin;
                double size = coverSize;
                if (fraction <= 0.5f) {
                    size += fraction * addSize;
                } else {
                    size += addSize - fraction * addSize;
                }
                if (isLogin) {
                    fractionCover = 1f - fraction;
                    fractionLogin = fraction;
                    if (fraction >= 0.5f) {
                        cover.registerRight(fractionCover * 100);
                    } else {
                        cover.loginRight(fractionLogin * 100);
                    }
                } else {
                    fractionCover = fraction;
                    fractionLogin = 1f - fraction;
                    if (fraction <= 0.5f) {
                        cover.registerLeft(fraction * 100);
                    } else {
                        cover.loginLeft((1f - fraction) * 100);
                    }
                }
                if (fraction >= 0.5f) {
                    showRegister(isLogin);
                }
                fractionCover = Double.valueOf(df.format(fractionCover));
                fractionLogin = Double.valueOf(df.format(fractionLogin));
                layout.setComponentConstraints(cover, "width " + size + "%, pos " + fractionCover + "al 0 n 100%");
                layout.setComponentConstraints(logReg, "width " + loginSize + "%, pos " + fractionLogin + "al 0 n 100%");
                bg.revalidate();
            }

            @Override
            public void end() {
                isLogin = !isLogin;
            }
        };
        Animator animator = new Animator(800, target);
        animator.setAcceleration(0.5f);
        animator.setDeceleration(0.5f);
        animator.setResolution(0);  //  for smooth animation
        bg.setLayout(layout);
        bg.setLayer(loading, JLayeredPane.POPUP_LAYER);
        bg.add(loading, "pos 0 0 100% 100%");
        bg.add(cover, "width " + coverSize + "%, pos 0al 0 n 100%");
        bg.add(logReg, "width " + loginSize + "%, pos 1al 0 n 100%"); //  1al as 100%
        cover.addEvent(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (!animator.isRunning()) {
                    animator.start();
                }
            }
        });
    }

    private void initLogin() {
        login.setLayout(new MigLayout("wrap", "push[center]push", "push[]25[]10[]10[]25[]push"));

        JLabel label = new JLabel("Sign In");
        label.setFont(new Font("sansserif", 1, 30));
        label.setForeground(new Color(30, 60, 121));
        login.add(label);

        // Campo Email
        emailField = new MyTextField();
        //emailField.setPrefixIcon(new ImageIcon( getClass().getResource("/main/java/view/icon/mail.png")));
        emailField.setHint("Email");
        login.add(emailField, "w 60%");

        //pass
        passwordField = new MyPasswordField();
        passwordField.setPrefixIcon(new ImageIcon("/main/java/view/icon/pass.png"));
        passwordField.setHint("Password");
        login.add(passwordField, "w 60%");

        // Botão "Entrar na Conta"
        JButton cmdForget = new JButton("Forgot your password ?");
        cmdForget.setForeground(new Color(100, 100, 100));
        cmdForget.setFont(new Font("sansserif", 1, 12));
        cmdForget.setContentAreaFilled(false);
        cmdForget.setCursor(new Cursor(Cursor.HAND_CURSOR));
        login.add(cmdForget);

        loginButton = new Button();
        loginButton.setBackground(new Color(35, 60, 121));
        loginButton.setForeground(new Color(250, 250, 250));
        loginButton.setText("SIGN IN");
        login.add(loginButton, "w 40%, h 40");
    }

    private void initRegister() {
        register.setLayout(new MigLayout("wrap", "push[center]push", "push[]35[]15[]15[]15[]15[]15[]15[]15[]15[]35[]push"));
        JLabel label = new JLabel("Create Account");
        label.setFont(new Font("sansserif", 1, 30));
        label.setForeground(new Color(30, 40, 112));
        register.add(label);

        // Campos com referências diretas
        txtFirstName = new MyTextField();
        txtFirstName.setPrefixIcon(new ImageIcon(("/main/java/view/icon/user.png")));
        txtFirstName.setHint("Nome");
        register.add(txtFirstName, "w 60%");

        txtLastName = new MyTextField();
        txtLastName.setPrefixIcon(new ImageIcon(("/main/java/view/icon/user.png")));
        txtLastName.setHint("Apelido");
        register.add(txtLastName, "w 60%");

        txtEmail = new MyTextField();
        txtEmail.setPrefixIcon(new ImageIcon("/main/java/view/icon/mail.png"));
        txtEmail.setHint("Email");
        register.add(txtEmail, "w 60%");

        txtPassword = new MyPasswordField();
        txtPassword.setPrefixIcon(new ImageIcon(("/main/java/view/icon/pass.png")));
        txtPassword.setHint("Password");
        register.add(txtPassword, "w 60%");

        txtBi = new MyTextField();
        txtBi.setPrefixIcon(new ImageIcon(("/main/java/view/icon/pass.png")));
        txtBi.setHint("BI");
        register.add(txtBi, "w 60%");

        txtPassport = new MyTextField();
        txtPassport.setPrefixIcon(new ImageIcon(("/main/java/view/icon/pass.png")));
        txtPassport.setHint("Passaporte");
        register.add(txtPassport, "w 60%");

        txtNuit = new MyTextField();
        txtNuit.setPrefixIcon(new ImageIcon(("/main/java/view/icon/pass.png")));
        txtNuit.setHint("Nuit");
        register.add(txtNuit, "w 60%");

        txtPhone = new MyTextField();
        txtPhone.setPrefixIcon(new ImageIcon(("/main/java/view/icon/mail.png")));
        txtPhone.setHint("Contacto(+258)");
        register.add(txtPhone, "w 60%");

        txtAddress = new MyTextField();
        txtAddress.setPrefixIcon(new ImageIcon(("/main/java/view/icon/mail.png")));
        txtAddress.setHint("Endereco");
        register.add(txtAddress, "w 60%");

        sigupButton = new Button();
        sigupButton.setBackground(new Color(30, 44, 121));
        sigupButton.setForeground(new Color(250, 250, 250));
        sigupButton.setText("SIGN UP");
        register.add(sigupButton, "w 40%, h 40");
    }

    public void clearRegisterFields() {
        txtFirstName.setText("");
        txtLastName.setText("");
        txtEmail.setText("");
        txtPassword.setText("");
        txtBi.setText("");
        txtPassport.setText("");
        txtNuit.setText("");
        txtPhone.setText("");
        txtAddress.setText("");
    }

    public String getEmail() {
        return emailField.getText().trim();
    }

    public String getPassword() {
        return new String(passwordField.getPassword());
    }

    public String getRegisterFirstName() {
        return txtFirstName.getText().trim();
    }

    public String getRegisterLastName() {
        return txtLastName.getText().trim();
    }

    public String getRegisterEmail() {
        return txtEmail.getText().trim();
    }

    public String getRegisterPassword() {
        return new String(txtPassword.getPassword());
    }

    public String getRegisterBiNumber() {
        return txtBi.getText().trim();
    }

    public String getRegisterPassportNumber() {
        return txtPassport.getText().trim();
    }

    public String getRegisterNuit() {
        return txtNuit.getText().trim();
    }

    public String getRegisterPhone() {
        return txtPhone.getText().trim();
    }

    public String getRegisterAddress() {
        return txtAddress.getText().trim();
    }


    // LISTENERS
    public void addLoginListener(ActionListener listener) {
        loginButton.addActionListener(listener);
    }

    public void addSigupListener(ActionListener listener) {
        sigupButton.addActionListener(listener);
    }

    public void loginReg() {
        initComponents2();
        initRegister();
        initLogin();
        login.setVisible(false);
        register.setVisible(true);
    }

    public void showRegister(boolean show) {
        if (show) {
            register.setVisible(true);
            login.setVisible(false);
        } else {
            register.setVisible(false);
            login.setVisible(true);
        }
    }

    public void initComponents2() {
        logReg = new JLayeredPane();
        login = new javax.swing.JPanel();
        register = new javax.swing.JPanel();

        logReg.setLayout(new java.awt.CardLayout());

        login.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout loginLayout = new javax.swing.GroupLayout(login);
        login.setLayout(loginLayout);
        loginLayout.setHorizontalGroup(
                loginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 327, Short.MAX_VALUE)
        );
        loginLayout.setVerticalGroup(
                loginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 300, Short.MAX_VALUE)
        );

        logReg.add(login, "card3");

        register.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout registerLayout = new javax.swing.GroupLayout(register);
        register.setLayout(registerLayout);
        registerLayout.setHorizontalGroup(
                registerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 327, Short.MAX_VALUE)
        );
        registerLayout.setVerticalGroup(
                registerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 300, Short.MAX_VALUE)
        );

        logReg.add(register, "card2");
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        bg = new JLayeredPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        //setUndecorated(true);

        bg.setBackground(new java.awt.Color(255, 255, 255));
        bg.setOpaque(true);

        javax.swing.GroupLayout bgLayout = new javax.swing.GroupLayout(bg);
        bg.setLayout(bgLayout);
        bgLayout.setHorizontalGroup(
            bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1083, Short.MAX_VALUE)
        );
        bgLayout.setVerticalGroup(
            bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 700, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(bg, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(bg)
        );

        pack();
        setLocationRelativeTo(null);
    }
}

