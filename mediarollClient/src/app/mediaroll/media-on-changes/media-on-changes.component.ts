import { Component, OnInit, OnChanges, SimpleChanges, SimpleChange, Input, Output, EventEmitter} from '@angular/core';
import { Media } from '../../models/media';
import { MediaService } from '../../services/media/media.service';
import swal from 'sweetalert2';

@Component({
  selector: 'app-media-on-changes',
  templateUrl: './media-on-changes.component.html',
  styleUrls: ['./media-on-changes.component.css']
})
export class MediaOnChangesComponent implements OnInit, OnChanges {
  @Input() mediasUploaded: Media[];

  @Output() notify: EventEmitter<string> = new EventEmitter<string>();

  constructor(private mediaService: MediaService) { }

  ngOnInit() {
  }

  ngOnChanges(changes: SimpleChanges) { 
  }

  showModal(el: HTMLImageElement) {
    $('#myModal').show();
    $('#modalContent').attr('src', el.src);
    $('#modalContent').attr('data-id', el.getAttribute('data-id'));
    $('#caption').html($(this).attr('alt'));
    $('.navbar').css('z-index', '0');
    $('#slide-out').css('z-index', '0');

  }

  closeModal() {
    $('#myModal').hide();
    $('.navbar').css('z-index', '');
    $('#slide-out').css('z-index', '');
  }

  deleteMedia() {
    swal({
      title: 'Etes-vous sure?',
      text: "Vous ne pourrez plus recuperer ce media !",
      type: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Supprimer !',
      cancelButtonText: 'Annuler !',
      confirmButtonClass: 'btn btn-success',
      cancelButtonClass: 'btn btn-danger',
      buttonsStyling: false,
      reverseButtons: true
    }).then((result) => {
      if (result.value) {
        const mediaId = $('#modalContent').attr('data-id');
        const isDeleted = this.mediaService.deleteMediaById(mediaId).subscribe(
          response => {
            swal(
              'Suppression !',
              'Le fichier a été supprimé !',
              'success'
            );
            this.closeModal();
            $('#' + mediaId).parent().remove();
            this.notify.emit(mediaId);
          },
          err => {
            console.log('Error occured.');
            swal(
              'Suppression !',
              'Une erreur est survenue !',
              'error'
            );
          }
        );
      // result.dismiss can be 'cancel', 'overlay',
      // 'close', and 'timer'
      } else if (result.dismiss === 'cancel') {
        swal(
          'Annulation',
          'La suppression a été annulé !',
          'error'
        );
      }
    });
  }

  mediaUrl(id: string) {
    return this.mediaService.buildUrlToMediaWithId(id);
  }
}
