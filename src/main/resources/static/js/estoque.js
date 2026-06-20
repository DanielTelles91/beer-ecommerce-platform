document.getElementById("filtro")
    .addEventListener("keyup", function() {

        let texto = this.value.toLowerCase();

        let linhas =
            document.querySelectorAll(
                "#tabelaEstoque tbody tr"
            );

        linhas.forEach(function(linha) {

            let conteudo =
                linha.textContent.toLowerCase();

            linha.style.display =
                conteudo.includes(texto)
                    ? ""
                    : "none";
        });
    });


document.getElementById("ordenacao")
    .addEventListener("change", function() {

        let tbody =
            document.querySelector(
                "#tabelaEstoque tbody"
            );

        let linhas =
            Array.from(
                tbody.querySelectorAll("tr")
            );

        let ordem = this.value;

        if (ordem === "critico") {

            linhas.sort(function(a, b) {

                let qtdA = parseInt(a.cells[2].innerText);
                let minA = parseInt(a.cells[3].innerText);

                let qtdB = parseInt(b.cells[2].innerText);
                let minB = parseInt(b.cells[3].innerText);

                let prioridadeA =
                    qtdA === 0 ? 3 :
                        qtdA <= minA ? 2 :
                            qtdA <= (minA * 2) ? 1 : 0;

                let prioridadeB =
                    qtdB === 0 ? 3 :
                        qtdB <= minB ? 2 :
                            qtdB <= (minB * 2) ? 1 : 0;

                return prioridadeB - prioridadeA;
            });

        } else {

            linhas.sort(function(a, b) {

                let textoA =
                    a.cells[0].innerText.toLowerCase();

                let textoB =
                    b.cells[0].innerText.toLowerCase();

                return ordem === "az"
                    ? textoA.localeCompare(textoB)
                    : textoB.localeCompare(textoA);
            });
        }

        if (ordem === "indisponivel") {

            linhas.sort(function(a, b) {

                let dispA =
                    a.cells[8].innerText
                        .trim()
                        .includes("Indisponível");

                let dispB =
                    b.cells[8].innerText
                        .trim()
                        .includes("Indisponível");

                return dispB - dispA;
            });
        }

        linhas.forEach(function(linha) {
            tbody.appendChild(linha);
        });

    });


function confirmarExclusao(botao) {

    let nome = botao.dataset.nome;

    return confirm(
        "Tem certeza que deseja excluir o estoque da cerveja:\n\n" +
        nome + " ?"
    );
}

function filtrarStatus(tipo) {

    let linhas =
        document.querySelectorAll(
            "#tabelaEstoque tbody tr"
        );

    linhas.forEach(function(linha) {

        let status =
            linha.cells[1].innerText
                .trim()
                .toLowerCase();

        let disponivel =
            linha.cells[8].innerText
                .trim()
                .toLowerCase();

        if (tipo === "todos") {

            linha.style.display = "";

        }
        else if (tipo === "baixo") {

            linha.style.display =
                status === "baixo"
                    ? ""
                    : "none";

        }
        else if (tipo === "critico") {

            linha.style.display =
                status === "crítico"
                    ? ""
                    : "none";

        }
        else if (tipo === "esgotado") {

            linha.style.display =
                status === "esgotado"
                    ? ""
                    : "none";

        }
        else if (tipo === "indisponivel") {

            linha.style.display =
                disponivel === "indisponível"
                    ? ""
                    : "none";

        }

    });
}
