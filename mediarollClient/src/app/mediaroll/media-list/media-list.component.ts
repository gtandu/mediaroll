import { Component, OnInit } from '@angular/core';
import { Media } from '../../models/media';
import { MediaService } from '../../services/media/media.service';
import { AuthentificationService } from '../../services/authentification.service';
import swal from 'sweetalert2';

@Component({
  selector: 'app-medialist',
  templateUrl: './media-list.component.html',
  styleUrls: ['./media-list.component.css']
})
export class MediaListComponent implements OnInit {
  medias: Media[] = [];

  constructor(private mediaService: MediaService) { }

  ngOnInit() {
    this.mediaService.getAllMedias().subscribe(medias => {
      this.medias = medias;
    });
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
