$(function() {
	// Deck initialization
	$.deck('.slide');
	$(document).bind('deck.change', function(event, from, to) {
		// console.log('Moving from slide ' + from + ' to ' + to);
		// switch (to) {
		// 	case 19:
		// 		break;
		// 	default:
		// 		break;
		// 	}
	});
	
	$('#style-themes').change(function() {
		$('#style-theme-link').attr('href', $(this).val());
	});
	
	$('#transition-themes').change(function() {
		$('#transition-theme-link').attr('href', $(this).val());
	});
});

