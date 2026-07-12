// valida se as senhas coincidem antes de submeter
document.querySelector('form').addEventListener('submit', function(e) {
    const nova = document.getElementById('novaSenha').value;
    const confirmar = document.getElementById('confirmarSenha').value;
    const erro = document.getElementById('senhaErro');
    if (nova !== confirmar) {
        e.preventDefault();
        erro.style.display = 'block';
    } else {
        erro.style.display = 'none';
    }
});