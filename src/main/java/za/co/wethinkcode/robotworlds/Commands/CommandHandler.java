package za.co.wethinkcode.robotworlds.Commands;

import org.json.JSONException;
import za.co.wethinkcode.robotworlds.Command;
import za.co.wethinkcode.robotworlds.Json.ResponseHandler;
import za.co.wethinkcode.robotworlds.Robot;

public class CommandHandler extends Command {
    private ResponseHandler response;

    public CommandHandler() {
        super("invalid");
        response = new ResponseHandler();
    }

    public boolean commandHandle(String command, Robot target) throws JSONException {
        if (command.equals("forward")) {
            ForwardCommand forwardCommand = new ForwardCommand(""); // Create an instance of ForwardCommand
            forwardCommand.setArgument(""); // Set the argument if required
            return forwardCommand.execute(target);
        } else if (command.equals("back")) {
            BackCommand backCommand = new BackCommand(""); // Create an instance of BackCommand
            backCommand.setArgument(""); // Set the argument if required
            return backCommand.execute(target);
        } else if (command.equals("turn")) {
            TurnCommand turnCommand = new TurnCommand(); // Create an instance of TurnCommand
            turnCommand.setArgument(""); // Set the argument if required
            return turnCommand.execute(target);
        } else if (command.equals("launch")) {
            LaunchCommand launchCommand = new LaunchCommand("", ""); // Create an instance of LaunchCommand
            launchCommand.setArgument(" "); // Set the argument if required
            return launchCommand.execute(target);
        } else if (command.equals("state")) {
            StateCommand stateCommand = new StateCommand(); // Create an instance of StateCommand
            return stateCommand.execute(target);
        } else {
            target.setStatus(response.invalidCommand());
            return true;
        }
    }

    @Override
    public boolean execute(Robot target) throws JSONException {
        return commandHandle(getCommand(), target);
    }

    public String getCommand() {
        return super.getCommand();
    }
}
