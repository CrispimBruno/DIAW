const BASE = "/api";

export function getSessao() {
  try {
    return JSON.parse(localStorage.getItem("sessao"));
  } catch {
    return null;
  }
}

export function salvarSessao(sessao) {
  localStorage.setItem("sessao", JSON.stringify(sessao));
}

export function limparSessao() {
  localStorage.removeItem("sessao");
}

export async function api(caminho, { metodo = "GET", corpo } = {}) {
  const sessao = getSessao();
  const cabecalhos = { "Content-Type": "application/json" };
  if (sessao) {
    cabecalhos.Authorization = `Bearer ${sessao.token}`;
  }
  let resposta;
  try {
    resposta = await fetch(BASE + caminho, {
    method: metodo,
    headers: cabecalhos,
    body: corpo ? JSON.stringify(corpo) : undefined
    });
  } catch {
    throw new Error("Não foi possível conectar à API. Verifique se o back-end está em execução na porta 8080.");
  }
  if (resposta.status === 204) {
    return null;
  }
  const dados = await resposta.json().catch(() => null);
  if (!resposta.ok) {
    if (resposta.status === 401 && sessao) {
      limparSessao();
      window.location.assign("/login");
    }
    const mensagem = {
      401: "Usuário ou senha inválidos, ou sessão expirada.",
      403: "Acesso negado pela API. Verifique a configuração de acesso do back-end.",
      500: "Erro interno da API. Verifique o terminal do back-end; se ele estiver desligado, inicie-o na porta 8080.",
      502: "A API está indisponível. Verifique se o back-end está em execução na porta 8080.",
      503: "A API está temporariamente indisponível. Tente novamente em instantes.",
      504: "A API demorou para responder. Verifique o terminal do back-end."
    }[resposta.status];
    throw new Error(dados?.erro ?? mensagem ?? `Não foi possível concluir a operação (HTTP ${resposta.status}).`);
  }
  return dados;
}
