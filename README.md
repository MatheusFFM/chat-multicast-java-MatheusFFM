# Chat Multicast Java

##AUTOR
*Matheus Felipe Ferreira Martins

##Comandos

- **CRIAR SALAS**:  /room ${room_name} | Esse comando cria uma nova sala com o nome igual ao valor em room_name, então retorna o endereço da sala.
- **LISTAR SALAS**: /list | Esse comando mostra todas as salas criadas.
- **ENTRAR NA SALA**: /join ${room_address} | Esse comando entra na sala com o **endereço** igual ao valor em room_address.
- **SAIR DA SALA**: /exit | Esse comando faz com que o usuário saia da sala, caso não esteja em uma, o programa é finalizado.
- **LISTAR MEMBROS DA SALA**: /members ${room_address} | Esse comando mostra todos os membros presentes na sala com o **endereço** igual ao valor em room_address.
- **ENVIAR MENSAGEM**: Uma vez dentro de uma sala, qualquer mensagem exceto o comando /exit mandam mensagem para a sala;

##Classes

- **SERVER:** Cria um server TCP que instancia a classe Connection. Possui a classe main do lado do servidor.
- **CONECTION:** Realiza o controle dos dados, recebe comandos de usuário a partir da classe Client, manipula ou cria salas e usuários e retorna um valor de resposta para a classe Client, podendo também ser um erro.
- **CLIENT:** Estabelece uma conexão com o servidor, recebe inputs do usuário e os manda para essa conexão, trata a resposta do usuário e controla a conexão multicast com as salas de chat. Possui a classe main do lado do cliente.
- **COMMANDS:** Possui os comandos do sistema.
- **USER:** Modelo do usuário, possuindo seu nome.
- **ROOM:** Modelo da sala, com seu nome, endereço e usuários.
- **IpAddress:* Modelo de endereço para gerar endereços multicast com mais facilidade.

##Execução - Server

- java network.Server

Única interação com o usuário é exibir a mensagem de servidor iniciado.

##Execução - Cliente

- java network.Client

Possui diversas interações com o usuário. Inicia pedindo um username e então mostra o menu de opções.
