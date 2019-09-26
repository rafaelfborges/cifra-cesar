# cifra-cesar
Minha solução em Java para o desafio Criptografia de Júlio Cesar proposta pela AceleraDev, na etapa do processo seletivo para uma bolsa de aceleração de PHP em parceria com a empresa VHSYS na grande Curitiba.

A proposta do desafio consistia em realizar uma requisição HTTP para uma determinada API, que por sua vez retornava um JSON. A partir deste JSON o objetivo era realizar os seguintes passos:
- Armazenar em arquivo, 'answer.json', o conteúdo da requisição.
- Ler o conteúdo deste arquivo e utilizar os campos correspondentes ao texto cifrado e a quantidade de 'casas' para decifrar a mensagem.
- Decifrar a mensagem e atualizar o arquivo no campo correspondende a mensagem decifrada.
- Gerar um resumo criptográfico utilizando SHA-1 e atualizar o arquivo no campo correspondente.
- Enviar o arquivo JSON para API como um formulário, utilizando o enctype 'multipart/form-data'. A resposta desta requisição é correção do desafio e respectiva nota.

Nota que tirei: 'Score 100'
