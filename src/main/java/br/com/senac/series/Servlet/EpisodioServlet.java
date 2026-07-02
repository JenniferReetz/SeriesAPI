package br.com.senac.series.Servlet;

import br.com.senac.series.DAO.EpisodioDAO;
import br.com.senac.series.Model.Episodio;
import br.com.senac.series.Model.Erro;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

@WebServlet("/episodios/*")
public class EpisodioServlet extends HttpServlet {

    private final EpisodioDAO dao = new EpisodioDAO();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/json;charset=UTF-8");

        String pathInfo = req.getPathInfo();
        String serieIdParam = req.getParameter("serieId");

        try {
            // GET /episodios?serieId=1
            if (serieIdParam != null) {
                int idSerie = Integer.parseInt(serieIdParam);
                List<Episodio> episodios = dao.listarPorSerie(idSerie);
                resp.getWriter().print(gson.toJson(episodios));
            }
            // GET /episodios
            else if (pathInfo == null || pathInfo.equals("/")) {
                List<Episodio> episodios = dao.listarTodos();
                resp.getWriter().print(gson.toJson(episodios));
            }
        } catch (Exception e) {
            Erro erro = enviarErro("Erro ao listar episódios", e);

            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().print(gson.toJson(erro));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");

        try {
            Episodio episodio = lerCorpoJson(req, Episodio.class);
            Episodio criado = dao.inserir(episodio);

            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().print(gson.toJson(criado));

        } catch (Exception e) {
            Erro erro = enviarErro("Erro ao inserir episódio", e);

            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().print(gson.toJson(erro));
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");

        try {
            Episodio episodio = lerCorpoJson(req, Episodio.class);
            boolean atualizou = dao.atualizar(episodio);

            if (atualizou) {
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            Erro erro = enviarErro("Erro ao atualizar episódio", e);

            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().print(gson.toJson(erro));
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        String pathInfo = req.getPathInfo();

        try {

            if (pathInfo != null && pathInfo.length() > 1) {
                int id = Integer.parseInt(pathInfo.substring(1));
                boolean excluiu = dao.excluirPorId(id);

                if (excluiu) {
                    resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
                } else {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                }
            }
        } catch (Exception e) {
            Erro erro = enviarErro("Erro ao excluir episódio", e);

            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().print(gson.toJson(erro));
        }
    }

    private <T> T lerCorpoJson(HttpServletRequest req, Class<T> classe) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader bf = req.getReader()) {
            String linha;

            while ((linha = bf.readLine()) != null) {
                sb.append(linha);
            }
        }
        return gson.fromJson(sb.toString(), classe);
    }
    private Erro enviarErro(String mensagem, Exception e) {
        return new Erro(mensagem, e.getMessage());
    }
}
