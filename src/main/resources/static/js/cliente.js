function mascaraCPF(campo) {
    let valor = campo.value.replace(/\D/g, ''); // Aceita apenas números

    valor = valor.substring(0, 11); // Máximo de 11 dígitos

    valor = valor.replace(/(\d{3})(\d)/, '$1.$2');
    valor = valor.replace(/(\d{3})\.(\d{3})(\d)/, '$1.$2.$3');
    valor = valor.replace(/(\d{3})\.(\d{3})\.(\d{3})(\d)/, '$1.$2.$3-$4');

    campo.value = valor;
}

function mascaraTelefone(campo) {
    let valor = campo.value.replace(/\D/g, ''); // Apenas números

    valor = valor.substring(0, 11); // DDD (2) + Número (9)

    if (valor.length > 2) {
        valor = valor.replace(/^(\d{2})(\d)/, '($1)$2');
    }

    if (valor.length > 7) {
        valor = valor.replace(/^(\(\d{2}\))(\d{5})(\d)/, '$1$2-$3');
    }

    campo.value = valor;
	}