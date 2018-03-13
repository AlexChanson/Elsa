package handler.concrete;

import beans.User;
import dao.BasicVirtualTable;
import handler.*;


public class CreateAccount implements Handler<RequestResult>{
    @Override
    public RequestResult handle(Command command) {
        Object s = command.getParameter("type");
        if (s != null && s.equals("createAccount")){
            try{
                boolean ok = makeAccount(command);
            }catch (Exception e){
                return new RequestError("userCreationError", e.getMessage());
            }
            return new RequestResult() {
                @Override
                public String toJson() {
                    return "{\"status\":\"success\"}";
                }
            };
        }
        return null;
    }

    private static boolean makeAccount(Command command) {
        String email = (String) command.getParameter("email");
        String nom = (String) command.getParameter("nom");
        String prenom = (String) command.getParameter("prenom");
        String password = (String) command.getParameter("password");

        User jeanPierre = new User(email, nom, prenom, Utility.hashSHA256(password));

        (new BasicVirtualTable<User>(User.class)).add(jeanPierre);

        return true;
    }
}
