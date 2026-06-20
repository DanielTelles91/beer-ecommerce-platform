document.getElementById("filtro")
    .addEventListener("keyup", function() {

        let texto = this.value.toLowerCase();

        let linhas =
            document.querySelectorAll(
                "#tabelaCervejarias tbody tr"
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
                "#tabelaCervejarias tbody"
            );

        let linhas =
            Array.from(
                tbody.querySelectorAll("tr")
            );

        let ordem = this.value;

        linhas.sort(function(a, b) {

            let textoA =
                a.cells[0].innerText.toLowerCase();

            let textoB =
                b.cells[0].innerText.toLowerCase();

            return ordem === "az"
                ? textoA.localeCompare(textoB)
                : textoB.localeCompare(textoA);
        });

        linhas.forEach(function(linha) {
            tbody.appendChild(linha);
        });
    });

function confirmarExclusao(botao) {

    let nome = botao.dataset.nome;

    return confirm(
        "Excluir " +
        nome +
        "?\n\nEsta ação não poderá ser desfeita."
    );
}