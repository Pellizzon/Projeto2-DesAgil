package br.edu.insper.al.matheusp1.projeto2.data;

import java.io.IOException;

import br.edu.insper.al.matheusp1.projeto2.data.model.LoggedInUser;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public Result.Success login() {

        try {
            // TODO: handle loggedInUser authentication
            LoggedInUser fakeUser =
                    new LoggedInUser(
                            java.util.UUID.randomUUID().toString(),
                            "Jane Doe");
            return new Result.Success<>(fakeUser);
        } catch (Exception e) {
            return new Result.Success<>(new IOException("Erro ao entrar", e));
        }
    }
}
