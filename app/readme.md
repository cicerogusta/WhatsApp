# WhatsApp Clone (Projeto Android Kotlin) - Em Desenvolvimento

Este é um projeto de aplicativo Android que implementa uma versão simplificada do WhatsApp. Atualmente, o projeto encontra-se em desenvolvimento e tem como objetivo permitir que os usuários façam login, se registrem, enviem e recebam mensagens, alterem seus dados, usem a câmera e a galeria para compartilhar imagens e vídeos. O aplicativo utilizará as seguintes tecnologias:

- Firebase para autenticação (login e registro de usuários) e armazenamento de dados.
- Arquitetura MVVM (Model-View-ViewModel) para uma separação clara de responsabilidades.
- Hilt para injeção de dependência.
- ViewPager e TabLayout para a navegação entre as principais funcionalidades do aplicativo.
- Navigation para a navegação entre os fragments.
- Data Binding para vincular os dados da ViewModel com o layout.

## Funcionalidades Planejadas

### 1. Login e Registro
Os usuários poderão fazer login ou se registrar no aplicativo usando seus endereços de e-mail e senhas. O Firebase será utilizado para autenticação e gerenciamento dos usuários.

### 2. Envio e Recebimento de Mensagens
Os usuários poderão enviar mensagens uns para os outros. As mensagens serão enviadas e recebidas em tempo real usando o Firebase Realtime Database.

### 3. Alteração dos Dados do Usuário
Os usuários poderão editar suas informações de perfil, como nome e foto de perfil. As alterações serão salvas no Firebase Realtime Database.

### 4. Uso do Hardware de Câmera e Galeria
Os usuários poderão compartilhar imagens e vídeos usando a câmera do dispositivo ou selecionando da galeria.

## Configuração do Projeto

1. Clone este repositório para sua máquina local usando o seguinte comando:
   ```
   git clone https://github.com/cicerogusta/WhatsApp
   ```

2. Abra o projeto no Android Studio.

3. Configure o projeto com as suas chaves de API do Firebase. Para isso, crie um projeto no [Firebase Console](https://console.firebase.google.com/), adicione o aplicativo Android e siga as instruções para baixar o arquivo de configuração `google-services.json`. Coloque esse arquivo na pasta `app` do projeto.

4. Agora você pode construir e executar o aplicativo em seu dispositivo ou emulador Android.

## Progresso Atual

O desenvolvimento deste projeto ainda está em andamento. As funcionalidades planejadas estão sendo implementadas e testadas. Fique atento para atualizações e novidades!

## Contribuição

Contribuições são bem-vindas! Se você quiser adicionar ou melhorar alguma funcionalidade, sinta-se à vontade para criar um pull request.

## Contato

Se você quiser entrar em contato comigo: ciceroandrade2525@gmail.com

---