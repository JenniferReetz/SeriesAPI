package br.com.senac.series.Servlet;

import br.com.senac.series.DAO.SerieDAO;
import br.com.senac.series.Model.Erro;
import br.com.senac.series.Model.Serie;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

@WebServlet("/series/*")
public class SerieServlet extends HttpServlet {

    private final SerieDAO dao = new SerieDAO();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/json;charset=UTF-8");

        String pathInfo = req.getPathInfo();

        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                List<Serie> series = dao.listarTodos();
                resp.getWriter().print(gson.toJson(series));

            } else {
                int id = Integer.parseInt(pathInfo.substring(1));
                Serie serie = dao.buscarPorId(id);

                if (serie != null) {
                    resp.getWriter().print(gson.toJson(serie));
                } else {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                }
            }
        } catch (Exception e) {
            Erro erro = enviarErro("Erro ao listar séries", e);

            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().print(gson.toJson(erro));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");

        try {
            Serie serie = lerCorpoJson(req, Serie.class);
            Serie criada = dao.inserir(serie);

            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().print(gson.toJson(criada));

        } catch (Exception e) {
            Erro erro = enviarErro("Erro ao inserir série", e);

            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().print(gson.toJson(erro));

        }

    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");

        try {
            Serie serie = lerCorpoJson(req, Serie.class);
            boolean atualizou = dao.atualizar(serie);

            if (atualizou) {
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }

        } catch (Exception e) {
            Erro erro = enviarErro("Erro ao atualizar série", e);

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
            Erro erro = enviarErro("Erro ao excluir série", e);

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
