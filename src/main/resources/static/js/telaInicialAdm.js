document.addEventListener("DOMContentLoaded", () => {

    const { vendasPorMes, topCervejas, porStatus } = window.dashboardData;


    if (!vendasPorMes || !topCervejas || !porStatus) {
        console.error("Dashboard data não carregou");
        return;
    }

    // ===== GRÁFICO VENDAS =====
    new Chart(document.getElementById('graficoVendas'), {
        type: 'bar',
        data: {
            labels: ['Jan', 'Fev', 'Mar', 'Abr', 'Mai', 'Jun', 'Jul', 'Ago', 'Set', 'Out', 'Nov', 'Dez'],
            datasets: [{
                label: 'Vendas (R$)',
                data: vendasPorMes,
                backgroundColor: 'rgba(25, 135, 84, 0.7)',
                borderRadius: 4
            }]
        },
        options: {
            responsive: true,
            plugins: { legend: { display: false } }
        }
    });

    // ===== TOP CERVEJAS =====
    new Chart(document.getElementById('graficoTop5'), {
        type: 'bar',
        data: {
            labels: topCervejas.map(i => i.rotulo.length > 20
                ? i.rotulo.substring(0, 20) + '...'
                : i.rotulo),
            datasets: [{
                label: 'Unidades vendidas',
                data: topCervejas.map(i => i.totalVendido),
                backgroundColor: ['#198754', '#0dcaf0', '#ffc107', '#dc3545', '#6f42c1'],
                borderRadius: 4
            }]
        },
        options: {
            indexAxis: 'y',  // faz as barras ficarem horizontais 
            responsive: true,
            plugins: {
                legend: { display: false }
            },
            scales: {
                x: { ticks: { color: '#aaa', font: { size: 10 } }, grid: { color: '#333' } },
                y: { ticks: { color: '#ccc', font: { size: 10 } }, grid: { display: false } }
            }
        }
    });

    // ===== STATUS =====
    const statusLabels = Object.keys(porStatus);
    const statusData = Object.values(porStatus);

    const statusColors = {
        CONFIRMADO: '#ffc107',
        SEPARANDO_PRODUTOS: '#0dcaf0',
        ENVIADO: '#0d6efd',
        ENTREGUE: '#198754',
        CANCELADO: '#dc3545'
    };

    new Chart(document.getElementById('graficoStatus'), {
        type: 'bar',
        data: {
            labels: statusLabels,
            datasets: [{
                label: 'Pedidos',
                data: statusData,
                backgroundColor: statusLabels.map(s => statusColors[s] || '#6c757d'),
                borderRadius: 4
            }]
        },
        options: {
            responsive: true,
            plugins: {
                legend: { display: false }
            },
            scales: {
                x: { ticks: { color: '#aaa', font: { size: 9 } }, grid: { display: false } },
                y: { ticks: { color: '#aaa', font: { size: 10 } }, grid: { color: '#333' } }
            }
        }
    });

});