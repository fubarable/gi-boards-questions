package com.boardsquestion;

public class App {
    public static void main(String[] args) {
        System.out.println("Hello from Java 21 Maven project.");
        javax.swing.SwingUtilities.invokeLater(() -> {
            new App().createAndShowGUI();
        });
    }

    private void createAndShowGUI() {
        // create Swing JFrame
        javax.swing.JFrame frame = new javax.swing.JFrame("Boards Question Template");
        frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        frame.add(new MainPanel());
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }

    // public int add(int left, int right) {
    //     return left + right;
    // }
}
