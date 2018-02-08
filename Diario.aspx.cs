using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using _3tn.MunicipioOnlineBusinessRules.Servico.DiarioOficial;
using _3tn.MunicipioOnlineBusinessRules.Servico.Global;
using _3tn.Web;
using WebRoleMunicipioOnline.Classes;

namespace WebRoleMunicipioOnline.Servicos.DiarioOficial
{
    public partial class Diario : Page
    {

        private EdicaoMensal eMensal = new EdicaoMensal();
        protected void Page_Load(object sender, EventArgs e)
        {
            if (!IsPostBack)
            {
                if (Request.QueryString["id"] != null)
                {
                    string anomes = Request.QueryString["id"];
                    CarregarEdicaoMes(anomes);
                }
                else
                {
                    if (ddlAno.Items.Count <= 1) populaComboAno();

                    CarregaUltimaEdicao();
                    ddlMes.Enabled = false;
                    ddlEdicao.Enabled = false;
                }

            }

            home_diario.NavigateUrl = Utils.getUrlCustom(this.Page, "/cidadao/diariooficial");
        }

        public void populaComboAno()
        {
            ddlAno.DataValueField = "Value";
            ddlAno.DataTextField = "Key";
            ddlAno.DataSource = CarregarComboAno();
            ddlAno.DataBind();
            ddlAno.Items.Insert(0, "Selecione");

        }
        public void populaComboMes()
        {
            eMensal.NuCnpj = Master.NuCnpj;
            eMensal.DtAno = ddlAno.SelectedItem.ToString();
            ddlMes.DataValueField = "NuMes";
            ddlMes.DataTextField = "DsMes";
            ddlMes.DataSource = eMensal.ObterMesAno();
            ddlMes.DataBind();
            ddlMes.Items.Insert(0, "Selecione");
        }
        public List<KeyValuePair<string, string>> CarregarComboAno()
        {
            try
            {
                eMensal.NuCnpj = Master.NuCnpj;
                return eMensal.ObterAnoEdicao();
            }
            catch (Exception e)
            {
                throw new Exception("Erro ao carregar o ano das edições " + e.Message);
            }
        }

        private void CarregarMesAno(string ano)
        {
            int paginas = 1;
            List<EdicaoMensal> EdicaoMesAno = new List<EdicaoMensal>();
            eMensal.NuCnpj = Master.NuCnpj;
            eMensal.DtAno = ano;
            EdicaoMesAno = eMensal.ObterMesAno();
            try
            {
                RepeaterHtmlTemplate repeaterHtml = new RepeaterHtmlTemplate("~/templates/Servicos/DiarioOficial/Edicoes.htm");
                repeaterHtml.SetDataSource<EdicaoMensal>(EdicaoMesAno);
                pnEdicoes.Controls.Clear();
                if (paginas >= 1 && EdicaoMesAno.Count > 0)
                {
                    pnEdicoes.Controls.Add(new LiteralControl(repeaterHtml.ToHtml()));
                }
                upDiarioEdicoes.Update();
            }
            catch (Exception e)
            {
                throw new Exception("Erro ao carregar parametros: " + e.Message);
            }
        }
        private void CarregaUltimaEdicao()
        {
            List<Arquivo> ultimaEdicao = null;

            eMensal.NuCnpj = Master.NuCnpj;
            ultimaEdicao = eMensal.ObterUltimoDiario();
            try
            {
                RepeaterHtmlTemplate repeaterHtml = new RepeaterHtmlTemplate("~/templates/Servicos/DiarioOficial/UltimaEdicao.htm");
                repeaterHtml.SetDataSource<Arquivo>(ultimaEdicao);
                pnUltimaEdicao.Controls.Clear();
                pnUltimaEdicao.Controls.Add(new LiteralControl(repeaterHtml.ToHtml()));
                upDiarioEdicoes.Update();
            }
            catch (Exception e)
            {
                throw new Exception("Erro ao carregar a última edição publicada: " + e.Message);
            }
        }
        private List<Arquivo> CarregarComboEdicao()
        {
            List<Arquivo> EdicaoMes = new List<Arquivo>();
            try
            {
                eMensal.NuCnpj = Master.NuCnpj;
                eMensal.NuAnoMes = ddlAno.SelectedValue.ToString() + "" + ddlMes.SelectedValue.ToString();
                EdicaoMes = eMensal.ObterDiarioMensal();
            }
            catch (Exception)
            {
                throw new Exception("Ocorreu um erro ao listar as edições do mês selecionado!");
            }

            return EdicaoMes;
        }
        private void CarregarEdicaoMes(string anomes)
        {
            List<Arquivo> ultimasEdicoes = null;

            eMensal.NuCnpj = Master.NuCnpj;
            eMensal.NuAnoMes = anomes;
            ultimasEdicoes = eMensal.ObterDiarioMensal();

            RepeaterHtmlTemplate repeaterHtml = new RepeaterHtmlTemplate("~/templates/Servicos/DiarioOficial/EdicoesMensais.htm");
            repeaterHtml.SetDataSource<Arquivo>(ultimasEdicoes);
            Response.Clear();
            Response.ContentType = "text/html";
            Response.Write(repeaterHtml.ToHtml().Replace("myCarousel", "carousel" + anomes));
            Response.End();
        }
        protected void btnBuscaEdicaoS_OnClick(object sender, EventArgs e)
        {
            if (!string.IsNullOrEmpty(txtBuscaPalavraChave.Text))
            {
                BuscarEdicao(txtBuscaPalavraChave.Text);
            }
        }
        private void BuscarEdicao(string pesquisa)
        {
            pnEdicoes.Controls.Clear();
            eMensal.DsExpressao = txtBuscaPalavraChave.Text;
            eMensal.NuCnpj = Master.NuCnpj;

            ////Pesquisar em todas as categorias = 1
            List<Arquivo> listaArquivos = eMensal.FiltrarEdicoes();
            RepeaterHtmlTemplate repeaterHtml = new RepeaterHtmlTemplate("~/templates/Servicos/DiarioOficial/FiltroEdicao.htm");
            repeaterHtml.SetDataSource<Arquivo>(listaArquivos);
            pnEdicoes.Controls.Add(new LiteralControl(repeaterHtml.ToHtml()));
            upDiarioEdicoes.Update();
        }
        protected void ddlAno_OnSelectedIndexChanged(object sender, EventArgs e)
        {
            //Caso tenha selecionado o ano
            if (ddlAno.SelectedValue != "Selecione")
            {
                ddlMes.Enabled = true;
                btnConfirmeBusca.Visible = true;
                if (ddlMes.Items.Count <= 1) populaComboMes();

            }
            else
            {
                ddlMes.Enabled = false;
                btnConfirmeBusca.Visible = false;
            }
        }
        protected void ddlMes_OnSelectedIndexChanged(object sender, EventArgs e)
        {
            if (ddlMes.SelectedValue != "Selecione")
            {
                //Popula as edições de acordo com o mês selecionado
                ddlEdicao.Items.Clear();
                ddlEdicao.Enabled = true;
                ddlEdicao.DataSource = CarregarComboEdicao();
                ddlEdicao.DataTextField = "NmArquivo";
                ddlEdicao.DataValueField = "GdArquivo";
                ddlEdicao.DataBind();
                ddlEdicao.Items.Insert(0, "Selecione");
            }
            else
            {
                ddlEdicao.Enabled = false;

            }
        }
        protected void btnConfirmeBusca_OnClick(object sender, EventArgs e)
        {
            //Verifica se os combos Ano,Mes e Edição estão preenchidos com valores diferentes de Zeros
            if (ddlAno.SelectedValue != "Selecione" && ddlMes.SelectedValue != "Selecione" && ddlEdicao.SelectedValue != "Selecione" && ddlEdicao.Enabled)
            {
                //Carregar a edição no container principal
                CarregaEdicaoSelecionada();
            }
            else if (ddlAno.SelectedValue != "Selecione" && ddlMes.SelectedValue == "Selecione" && (ddlEdicao.Enabled == false))
            {
                //Carrega todos os meses do ano selecionado
                CarregarMesAno(ddlAno.SelectedItem.ToString());
            }

        }
        protected void CarregaEdicaoSelecionada()
        {
            List<Arquivo> edicaoSelecionada = null;

            eMensal.NuCnpj = Master.NuCnpj;
            eMensal.GdArquivo = ddlEdicao.SelectedValue.ToString() + ".pdf";
            try
            {
                edicaoSelecionada = eMensal.ObterEdicaoSelecionada();
                RepeaterHtmlTemplate repeaterHtml = new RepeaterHtmlTemplate("~/templates/Servicos/DiarioOficial/UltimaEdicao.htm");
                repeaterHtml.SetDataSource<Arquivo>(edicaoSelecionada);
                pnUltimaEdicao.Controls.Clear();
                pnUltimaEdicao.Controls.Add(new LiteralControl(repeaterHtml.ToHtml()));
                upDiarioEdicoes.Update();
            }
            catch (Exception)
            {
                throw new Exception("Erro na tentativa de visualizar a edição selecionada!");
            }
        }


    }
}