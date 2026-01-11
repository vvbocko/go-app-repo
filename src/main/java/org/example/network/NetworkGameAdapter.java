package org.example.network;

import org.example.Board;
import org.example.BoardUpdater;
import org.example.Point;
import org.example.Stone;
import org.example.ui.GameViewController;

public class NetworkGameAdapter implements GameAdapter, ServerMessageListener {

    private final GameClient client;
    private Stone playerColor;
    private final Board board;
    private final GameViewController gui;
    private BoardUpdater boardUpdater;
    private boolean receivingBoard = false;
    private StringBuilder boardBuffer = new StringBuilder();
    private String pendingBlackScore = "";
    private String pendingWhiteScore = "";


    public NetworkGameAdapter(GameClient client, GameViewController gui) {
        this.client = client;
        this.gui = gui;
        this.board = gui.getBoard();
        this.boardUpdater = new BoardUpdater(board);

        client.setServerMessageListener(this);
    }

    @Override
    public void onServerMessage(String msg) {
        javafx.application.Platform.runLater(() -> {

            if (msg.equals("BOARD_START")) {
                receivingBoard = true;
                boardBuffer.setLength(0);
                return;
            }

            if (msg.equals("BOARD_END")) {
                receivingBoard = false;
                boardUpdater.updateFromAscii(boardBuffer.toString());
                gui.refresh();
                return;
            }

            if (receivingBoard) {
                boardBuffer.append(msg).append("\n");
                return;
            }

            if (msg.startsWith("You are playing as:")) {
                playerColor = msg.contains("BLACK") ? Stone.BLACK : Stone.WHITE;
                gui.setStatus("You play as: " + playerColor);
            }
            else if (msg.startsWith("Your turn")) {
                gui.setMyTurn(true);
                gui.setStatus("Your turn");
            }
            else if (msg.startsWith("Waiting")) {
                gui.setMyTurn(false);
                gui.setStatus(msg);
            }
            else if (msg.startsWith("CAPTURES:")) {
                String[] parts = msg.substring(9).split(",");
                int blackCaptures = Integer.parseInt(parts[0]);
                int whiteCaptures = Integer.parseInt(parts[1]);
                gui.setCapturesDisplay(blackCaptures, whiteCaptures);
            }
            else if (msg.equals("GAME_OVER")) {
                gui.setMyTurn(false);
                pendingBlackScore = "";
                pendingWhiteScore = "";
            }
            else if (msg.startsWith("SCORE_BLACK:")) {
                pendingBlackScore = "BLACK: " + msg.substring(12);
            }
            else if (msg.startsWith("SCORE_WHITE:")) {
                pendingWhiteScore = "WHITE: " + msg.substring(12);
            }
            else if (msg.startsWith("WINNER:")) {
                String winner = msg.substring(7);

                if (!pendingBlackScore.isEmpty() && !pendingWhiteScore.isEmpty()) {
                    gui.showGameOver(pendingBlackScore, pendingWhiteScore, "Winner: " + winner);
                } else {
                    gui.showMessage("GAME OVER\nWinner: " + winner);
                }
            }
            else if (msg.startsWith("INVALID")) {
                gui.showMessage(msg);
            }
        });
    }

    @Override
    public void playMove(Point p) {
        if (!gui.isMyTurn()) {
            System.out.println("Not your turn");
            return;
        }
        String move = convertPoint(p);
        System.out.println("Sending move: " + move);
        client.sendMessage(move);
    }

    @Override
    public void pass() {
        client.sendMessage("PASS");
    }

    public void surrender() {
        client.sendMessage("SURRENDER");
    }

    @Override
    public Stone getColor() {
        return playerColor;
    }

    private String convertPoint(Point p) {
        char col = (char)('A' + p.x());
        String row = String.valueOf(p.y() + 1);
        return col + row;
    }
}

