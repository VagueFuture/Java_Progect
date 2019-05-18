
function connect() {
    var socket = new SockJS('/chat-messaging');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function(frame) {
        console.log("connected: " + frame);
        stompClient.subscribe('/chat/messages', function(response) {
            console.log(response);
            console.log("+");
            var data = JSON.parse(response.body);
            //draw("right", data.message);
            draw("right", data.message);
        });
    });
}

function draw(side, text) {
    console.log("drawing...");
    var $message;
    $message = $($('.message_template').clone().html());
    $message.addClass(side).find('.text').html(text);
    $('.messages').append($message);
    return setTimeout(function () {
        return $message.addClass('appeared');
    }, 0);
}
function disconnect(){
    stompClient.disconnect();
}
function sendMessage(){
    stompClient.send("/app/message", {}, JSON.stringify({'message': $("#message_input_value").val()}));
    //draw("right", $("#message_input_value").val());
}